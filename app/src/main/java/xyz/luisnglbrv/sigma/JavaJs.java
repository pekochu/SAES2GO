package xyz.luisnglbrv.sigma;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by pekochu on 10/01/18.
 */

public class JavaJs {
    private static JavaJs instancia = new JavaJs();
    private String contenido = "";
    private boolean estadoBloqueado = false;
    private int opcionNavegacion = 0;
    private WebView pagina = null;
    private WebView webReferencias = null;

    public static JavaJs getInstancia(){
        return instancia;
    }

    private String getMensaje(String parametro) {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Contenido = [").append(parametro).append("]");
        return localStringBuilder.toString();
    }

    @JavascriptInterface
    public String getContenido(){
        if ((!this.estadoBloqueado) && (this.contenido != null)) {}
        return this.contenido;
    }
}
