/**********************************************************************
 *
 * Copyright (c) 2022 Olaf Willuhn
 * All rights reserved.
 * 
 * This software is copyrighted work licensed under the terms of the
 * Jameica License.  Please consult the file "LICENSE" for details. 
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.paypal.transport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.utils.Base64;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.willuhn.annotation.Lifecycle;
import de.willuhn.annotation.Lifecycle.Type;
import de.willuhn.io.IOUtil;
import de.willuhn.jameica.hbci.HBCIProperties;
import de.willuhn.jameica.hbci.paypal.Plugin;
import de.willuhn.jameica.hbci.paypal.domain.ApiAuth;
import de.willuhn.jameica.hbci.paypal.domain.BalanceResult;
import de.willuhn.jameica.hbci.paypal.domain.TransactionDetails;
import de.willuhn.jameica.hbci.paypal.domain.TransactionResult;
import de.willuhn.jameica.hbci.rmi.Konto;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.jameica.system.Settings;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * Uebernimmt die Datenuebertragung.
 */
@Lifecycle(Type.CONTEXT)
public class TransportService
{
  private final static Settings settings = Application.getPluginLoader().getPlugin(Plugin.class).getResources().getSettings();
  private final static I18N i18n = Application.getPluginLoader().getPlugin(Plugin.class).getResources().getI18N();
  
  public final static String DF_ISO8601 = "yyyy-MM-dd'T'HH:mm:ssX";
  public final static String DF_ISO8601_SALDO = "yyyy-MM-dd'T'HH:mm:ss";
  
  private CloseableHttpClient client = null;
  private final ObjectMapper mapper = new ObjectMapper();
  
  /**
   * Initialisiert den Service.
   */
  @PostConstruct
  private void init()
  {
    Logger.info("init paypal transport service");
    this.client = HttpClients.createDefault();
  }
  
  /**
   * Beendet den Service.
   */
  @PreDestroy
  private void shutdown()
  {
    Logger.info("shutting down paypal transport service");
    try
    {
      IOUtil.close(this.client);
    }
    finally
    {
      this.client = null;
    }
  }
  
  /**
   * Führt das Login für das Konto durch.
   * @param konto das Konto.
   * @return das Ergebnis der Authentifizierung.
   * @throws ApplicationException wenn die Ausführung fehlschlug.
   * @throws ApiException wenn die Ausführung fehlschlug.
   */
  public ApiAuth login(Konto konto) throws ApplicationException, ApiException
  {
    try
    {
      Logger.info("perfoming login");
      final String clientId = konto.getMeta(Plugin.META_PARAM_API_CLIENTID,null);
      final String secret   = konto.getMeta(Plugin.META_PARAM_API_SECRET,null);
      if (StringUtils.trimToNull(clientId) == null)
        throw new ApplicationException(i18n.tr("Bitte geben Sie die API Client-ID in den Synchronisationsoptionen ein."));
      if (StringUtils.trimToNull(secret) == null)
        throw new ApplicationException(i18n.tr("Bitte geben Sie das API Secret in den Synchronisationsoptionen ein."));
      
      final Map<String,String> params = new HashMap<>();
      params.put("grant_type","client_credentials");
      final HttpPost post = this.createPost(this.createUri("/v1/oauth2/token",null),params);
      byte[] encodedAuth = Base64.encodeBase64((clientId + ":" + secret).getBytes(StandardCharsets.ISO_8859_1));
      post.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + new String(encodedAuth));
      
      final ApiAuth auth = this.request(post,null,ApiAuth.class);
      if (auth == null || StringUtils.trimToNull(auth.access_token) == null)
        throw new ApplicationException("Login fehlgeschlagen");
      
      Logger.info("login successful, token expiry: " + new Date(System.currentTimeMillis() + (auth.expires_in*1000L)));
      return auth;
    }
    catch (ApiException ae)
    {
      throw ae;
    }
    catch (ApplicationException ae2)
    {
      throw ae2;
    }
    catch (OperationCanceledException oce)
    {
      throw oce;
    }
    catch (Exception e)
    {
      Logger.error("unable to create GET request",e);
      throw new ApplicationException(i18n.tr("Fehler beim Erstellen der Abfrage: {0}",e.getMessage()));
    }
  }
  
  /**
   * Liefert die Transaktionen für den Zugang.
   * 
   * @param auth  der Zugang.
   * @param start Start-Datum für die Abfrage.
   * @return die Liste der Transaktionen.
   * @throws ApplicationException wenn die Ausführung fehlschlug.
   * @throws ApiException         wenn die Ausführung fehlschlug.
   */
  public List<TransactionDetails> getTransactions(ApiAuth auth, Date start) throws ApplicationException, ApiException
  {
    if (start == null)
      throw new ApplicationException("Kein Startdatum angegeben");
    List<TransactionDetails> result = new ArrayList<>();
    ZonedDateTime startDate = start.toInstant().atZone(ZoneId.systemDefault());
    do
    {
      final Map<String, String> params = new HashMap<>();
      params.put("start_date", startDate.format(DateTimeFormatter.ofPattern(DF_ISO8601)));
      if (startDate.plusDays(30).isAfter(ZonedDateTime.now()))
      {
	params.put("end_date", ZonedDateTime.now().format(DateTimeFormatter.ofPattern(DF_ISO8601)));
      } else
      {
	params.put("end_date", startDate.plusDays(30).format(DateTimeFormatter.ofPattern(DF_ISO8601)));
      }
      params.put("balance_affecting_records_only", "Y");
      params.put("fields", "all");
      params.put("page", "1");
      params.put("page_size", "500");
      final HttpGet get = this.createGet(this.createUri("/v1/reporting/transactions", params));
      TransactionResult transactionResult = request(get, auth, TransactionResult.class);
      if (transactionResult != null && transactionResult.transaction_details != null)
      {
	result.addAll(transactionResult.transaction_details);
      }
      startDate = startDate.plusDays(30);
    } while (startDate.isBefore(ZonedDateTime.now()));
    return result;
  }
  
  /**
   * Liefert die aktuellen Salden.
   * @param auth der Zugang.
   * @return die Salden.
   * @throws ApplicationException wenn die Ausführung fehlschlug.
   * @throws ApiException wenn die Ausführung fehlschlug.
   */
  public BalanceResult getBalances(ApiAuth auth) throws ApplicationException, ApiException
  {
    final DateFormat df = new SimpleDateFormat(DF_ISO8601);
    final Map<String,String> params = new HashMap<>();
    params.put("as_of_time",df.format(new Date())); // Immer das aktuelle Datum
    params.put("currency_code",HBCIProperties.CURRENCY_DEFAULT_DE); // Wir unterstützen ja eh nur EUR
    
    final HttpGet get = this.createGet(this.createUri("/v1/reporting/balances",params));
    return this.request(get,auth,BalanceResult.class);
  }
  
  /**
   * Erzeugt die Request-URI.
   * @param path der Pfad.
   * @param params optionale Request-Parameter.
   * @return die erzeugte URI.
   * @throws ApplicationException
   */
  private URI createUri(String path,Map<String,String> params) throws ApplicationException
  {
    final URIBuilder b = new URIBuilder();
    b.setScheme("https");
    b.setHost(this.getApiEndpoint().getHostname());
    b.setPath((path.startsWith("/") ? "" : "/") + path);
    if (params != null)
    {
      for (Entry<String,String> e:params.entrySet())
      {
        b.addParameter(e.getKey(),e.getValue());
      }
    }

    try
    {
      return b.build();
    }
    catch (URISyntaxException e)
    {
      Logger.error("unable to create request uri for path " + path,e);
      throw new ApplicationException(i18n.tr("Fehler beim Erstellen der Abfrage: {0}",e.getMessage()));
    }
  }
  
  /**
   * Erzeugt einen HTTP GET-Request.
   * @param uri die URI.
   * @return der erzeugte Request.
   */
  private HttpGet createGet(URI uri)
  {
    return new HttpGet(uri);
  }
  
  /**
   * Erzeugt einen HTTP POST-Request.
   * @param uri die URI.
   * @param params die Parameter.
   * @return der erzeugte Request.
   */
  private HttpPost createPost(URI uri, Map<String,String> params)
  {
    final HttpPost post = new HttpPost(uri);
    final List<NameValuePair> pairs = new ArrayList<>();
    if (params != null)
    {
      for (Entry<String,String> e:params.entrySet())
      {
        pairs.add(new BasicNameValuePair(e.getKey(),e.getValue()));
      }
    }
    
    post.setEntity(new UrlEncodedFormEntity(pairs));
    return post;
  }
  
  /**
   * Fuehrt einen Request aus.
   * @param <T> der Response-Typ.
   * @param request der Request.
   * @param auth die optionale Authentifizierung.
   * @param type der Response-Typ.
   * @return die deserialisierten Antwort-Daten.
   * @throws ApplicationException
   * @throws ApiException
   */
  private <T> T request(HttpUriRequestBase request, ApiAuth auth, Class<T> type) throws ApplicationException, ApiException
  {
    try
    {
      Logger.info("executing request to: " + request.getUri());
      
      if (auth != null)
        request.addHeader(HttpHeaders.AUTHORIZATION,"Bearer " + auth.access_token);
      
      request.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
      request.setHeader(HttpHeaders.ACCEPT,"application/json");
      request.setHeader(HttpHeaders.ACCEPT_LANGUAGE,"de_DE");
      
      final Object result = this.client.execute(request, response -> {
        final int status = response.getCode();

        final String json = this.read(response.getEntity().getContent());
        
        if (status > 299)
        {
          String msg = status + ": " + response.getReasonPhrase();
          Logger.error("got http status: " + msg);
          Logger.error("json response: " + json);
          
          // Checken, ob wir den Fehler lesen koennen
          try
          {
            ApiException error = this.mapper.readValue(json, ApiException.class);
            error.httpStatus = status;
            error.httpMessage = response.getReasonPhrase();
            return error;
          }
          catch (Exception e)
          {
          }
        }
        return this.mapper.readValue(json, type);
      });

      if (result instanceof ApiException)
        throw (ApiException) result;

      if (result instanceof ApplicationException)
        throw (ApplicationException) result;
      
      return (T) result;
    }
    catch (ApiException ae)
    {
      throw ae;
    }
    catch (ApplicationException ae2)
    {
      throw ae2;
    }
    catch (OperationCanceledException oce)
    {
      throw oce;
    }
    catch (Exception e)
    {
      Logger.error("unable to create GET request",e);
      throw new ApplicationException(i18n.tr("Fehler beim Erstellen der Abfrage: {0}",e.getMessage()));
    }
  }

  /**
   * Liest die JSON-Daten aus dem Stream und loggt sie.
   * @param is der Stream.
   * @return die gelesenen Daten.
   * @throws IOException
   */
  private String read(InputStream is) throws IOException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    IOUtil.copy(is,bos);
    
    final String s = bos.toString("UTF-8");
    Logger.debug("response: " + s);
    return s;
  }
  
  /**
   * Liefert den aktuell konfigurierten API-Endpunkt.
   * @return der aktuell konfigurierte API-Endpunkt.
   */
  private ApiEndpoint getApiEndpoint()
  {
    return ApiEndpoint.valueOf(settings.getString("endpoint",ApiEndpoint.LIVE.name()));
  }
  
}


