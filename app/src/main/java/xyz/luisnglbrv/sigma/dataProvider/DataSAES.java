package xyz.luisnglbrv.sigma.dataProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import xyz.luisnglbrv.sigma.Alumno;
import xyz.luisnglbrv.sigma.ETS;
import xyz.luisnglbrv.sigma.EscuelaActivity;
import xyz.luisnglbrv.sigma.LoginSaes;
import xyz.luisnglbrv.sigma.MainActivity;
import xyz.luisnglbrv.sigma.Preferencias;
import xyz.luisnglbrv.sigma.Reinscripcion;
import xyz.luisnglbrv.sigma.SetearDatos;
import xyz.luisnglbrv.sigma.objects.MateriaCalificaciones;
import xyz.luisnglbrv.sigma.objects.MateriaHorario;

/**
 * Created by pekochu on 08/09/17.
 */

public class DataSAES extends AsyncTask<Void, Integer, Void> {

    private final String TAG = DataSAES.class.getSimpleName();
    private Integer interval = 12;
    private int asyncTimeout = 60*1000;
    private Context mContext;
    public static List<String> listValidation;
    private static List<List<String>> listaCita = null;
    private static List<MateriaHorario> listaHorario = null;
    private static List<List<String>> listaAcademico = null;
    private static List<MateriaCalificaciones> listaOrdinario = null;
    private static List<String> listaSemestre = null;
    private static List<List<List<String>>> listaKardex = null;
    // vars
    private String saesUrl = "about:blank";
    private static String saesCookie = null;
    private Boolean sessionValid = false;
    // jSoup documents
    private Document saesSession;
    private Document saesKardex;
    private Document saesAcademico;
    private Document saesHorario;
    private Document saesOrdinario;
    private Document saesCita;
    private Document saesETS;
    // Clases
    private Preferencias sharedPrefs;
    private CookieSyncManager cookieSyncMngr;
    private CookieManager cookieManager;

    public DataSAES(Context context, int from){
        this.mContext = context;
        if(from != 1) {
            sharedPrefs = new Preferencias(this.mContext);
            cookieSyncMngr = CookieSyncManager.createInstance(this.mContext);
            cookieManager = CookieManager.getInstance();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        saesUrl = sharedPrefs.getURL();
        if(checkCookie(saesUrl))
            if(validarSesion()){
                publishProgress(interval);
                initLists();
                publishProgress(interval*2);
                fillData();
                publishProgress(interval*6);
                wipeLists();
                getKardex();
                getEstado();
                publishProgress(interval*7);
                getHorario();
                getCalificaciones();
                getCita();
                getETS();
                publishProgress((interval*8)+4);
            }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        SetearDatos.actualizarBarra(values[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
        if(!sessionValid){
            sharedPrefs.setIsValid(false);
            Log.d(TAG, "Sesión no valida.");
            wipeCookie(saesUrl);

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if(sharedPrefs.getURL() == null){
                        Toast.makeText(mContext, "¡Escoge tu unidad académica!",
                                Toast.LENGTH_SHORT).show();
                        mContext.startActivity(new Intent(mContext, EscuelaActivity.class));
                    }else
                        mContext.startActivity(new Intent(mContext, LoginSaes.class));
                    ((Activity)mContext).finish();
                }
            }, 600);
        }else{
            sharedPrefs.setIsValid(true);
            Log.d(TAG, "Sesión válida.");
            // progressDialog.dismiss();
            mContext.startActivity(new Intent(mContext, MainActivity.class));
            ((Activity)mContext).finish();
        }
    }

    private boolean validarSesion(){
        Log.d(TAG, "Verficando sesión.");
        Boolean valid = false;
        try {
            if(sharedPrefs.getSsl()){
                saesSession = Jsoup.connect(saesUrl+"/Alumnos/default.aspx").timeout(asyncTimeout).
                        header("Cookie", saesCookie).validateTLSCertificates(false).get();
            }else{
                saesSession = Jsoup.connect(saesUrl+"/Alumnos/default.aspx").timeout(asyncTimeout).
                        header("Cookie", saesCookie).get();
            }
            valid = saesSession.select("span#ctl00_mainCopy_FormView1_nombrelabel").hasText();
            listValidation = new ArrayList<>();
            listValidation.add(saesSession.select("span#ctl00_mainCopy_FormView1_nombrelabel").text());
            if(valid) sharedPrefs.setNombre(saesSession.select("span#ctl00_mainCopy_FormView1_nombrelabel").text());
            Log.d(TAG, "Sesión verificada.");
        } catch (Exception e) {
            Log.e(TAG, "Error verificando sesión: "+e.getMessage()+".");
            e.printStackTrace();
        }
        sessionValid = valid;
        return (valid);
    }

    private void initLists(){
        /* HORARIO */
        listaHorario = new ArrayList<>();
        listaHorario.add(new MateriaHorario("", "No hay información para mostrar." +
                "\n\nRazones:\n\n - No estás inscrito.\n\n" +
                " - El periodo ordinario ha finalizado.\n\n - Estás dado de baja.", "",
                "","","","","","","",""));
        /* CALIFICACIONES */
        listaOrdinario = new ArrayList<>();
        listaOrdinario.add(new MateriaCalificaciones("", "No hay información para mostrar." +
                "\n\nRazones:\n\n" +
                " - No estás inscrito.\n\n - No has evaluado a tus profesores.\n\n" +
                " - El periodo ordinario ha finalizado.\n\n - Estás dado de baja.",
                "","","","", ""));
        /* KARDEX */
        listaKardex = new ArrayList<>();
        listaSemestre = new ArrayList<>();
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());

        listaKardex.get(0).add(new ArrayList<String>()); // # Clave
        listaKardex.get(1).add(new ArrayList<String>()); // # Materia
        listaKardex.get(2).add(new ArrayList<String>()); // # Fecha
        listaKardex.get(3).add(new ArrayList<String>()); // # Periodo
        listaKardex.get(4).add(new ArrayList<String>()); // # Tipo de evaluación
        listaKardex.get(5).add(new ArrayList<String>()); // # Calificación

        listaKardex.get(0).get(0).add("");
        listaKardex.get(1).get(0).add("No hay información para mostrar.\n\nRazones:\n\n" +
                " - Aún vas en primer semestre.\n\n" +
                " - Te diste de baja antes de completar un semestre.");
        listaKardex.get(2).get(0).add("");
        listaKardex.get(3).get(0).add("");
        listaKardex.get(4).get(0).add("");
        listaKardex.get(5).get(0).add("");
        /* REINSCRIPCIÓN */
        listaCita = new ArrayList<>();
        for(int i = 0; i < 3; i++)
            listaCita.add(new ArrayList<String>());
        // # Cita
        for(int i = 0; i < 5; i++)
            listaCita.get(0).add("No disponible");
        // # Carrera
        for(int i = 0; i < 6; i++)
            listaCita.get(1).add("N/D");
        // # Trayectoria
        for(int i = 0; i < 7; i++)
            listaCita.get(2).add("N/D");
        /* ACADEMICO */
        listaAcademico = new ArrayList<>();
        listaAcademico.add(new ArrayList<String>());
        listaAcademico.add(new ArrayList<String>());
        listaAcademico.get(0).add("");
        listaAcademico.get(1).add("¡Felicidades! No tienes materias reprobadas.");
        /* Desfasadas */
        listaAcademico.add(new ArrayList<String>());
        listaAcademico.add(new ArrayList<String>());
        listaAcademico.get(2).add("");
        listaAcademico.get(3).add("¡Felicidades! No tienes materias desfasadas.");
        /* No cursadas */
        listaAcademico.add(new ArrayList<String>());
        listaAcademico.add(new ArrayList<String>());
        listaAcademico.get(4).add("");
        listaAcademico.get(5).add("¡Felicidades! No tienes materias no cursadas.");
        /* FIN */
    }

    private void fillData(){
        String kardexSuffix = "/Alumnos/boleta/kardex.aspx";
        String academicoSuffix = "/Alumnos/boleta/Estado_Alumno.aspx";
        String horarioSuffix = "/Alumnos/Informacion_semestral/Horario_Alumno.aspx";
        String ordinarioSuffix = "/Alumnos/Informacion_semestral/calificaciones_sem.aspx";
        String citaSuffix = "/Alumnos/Reinscripciones/fichas_reinscripcion.aspx";
        String etsSuffix = "/Alumnos/ETS/calificaciones_ets.aspx";

        try {
            if (sharedPrefs.getSsl()) {
                saesKardex = Jsoup.connect(saesUrl+kardexSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).validateTLSCertificates(false).get();
                saesAcademico = Jsoup.connect(saesUrl+academicoSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).validateTLSCertificates(false).get();
                saesHorario = Jsoup.connect(saesUrl+horarioSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).validateTLSCertificates(false).get();
                saesOrdinario = Jsoup.connect(saesUrl+ordinarioSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).validateTLSCertificates(false).get();
                saesCita = Jsoup.connect(saesUrl+citaSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).validateTLSCertificates(false).get();
                saesETS = Jsoup.connect(saesUrl+etsSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).validateTLSCertificates(false).get();
            } else {
                saesKardex = Jsoup.connect(saesUrl+kardexSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).get();
                saesAcademico = Jsoup.connect(saesUrl+academicoSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).get();
                saesHorario = Jsoup.connect(saesUrl+horarioSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).get();
                saesOrdinario = Jsoup.connect(saesUrl+ordinarioSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).get();
                saesCita = Jsoup.connect(saesUrl+citaSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).get();
                saesETS = Jsoup.connect(saesUrl+etsSuffix).timeout(asyncTimeout).
                        header("Cookie", saesCookie).get();
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }

        return;
    }

    private void getCalificaciones(){
        Log.d(TAG, "Obtener calificaciones.");
        Element tbody;
        Iterator<Element> td;

        if(saesOrdinario.select("table#ctl00_mainCopy_GV_Calif").hasText()){
            listaOrdinario.clear();
            Element table = saesOrdinario.select("table#ctl00_mainCopy_GV_Calif").first();
            tbody = table.select("tbody").first();
            td = tbody.select("td").iterator();

            while(td.hasNext()){
                listaOrdinario.add(new MateriaCalificaciones(td.next().text(),
                        td.next().text(),td.next().text(),td.next().text(), td.next().text(),
                        td.next().text(), td.next().text()));
            }
        }
    }

    private void getKardex(){
        Log.d(TAG, "Obteniendo datos del kardex.");
        int semestres = 0;
        String carrera = "ND", plan = "ND", promedio = "ND", escuela="ND";
        Element span, center, table, tbody;
        Iterator<Element> td;

        listaSemestre.clear();
        listaKardex.clear();
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.add(new ArrayList<List<String>>());
        listaKardex.get(0).add(new ArrayList<String>()); // # Clave
        listaKardex.get(1).add(new ArrayList<String>()); // # Materia
        listaKardex.get(2).add(new ArrayList<String>()); // # Fecha
        listaKardex.get(3).add(new ArrayList<String>()); // # Periodo
        listaKardex.get(4).add(new ArrayList<String>()); // # Tipo de evaluación
        listaKardex.get(5).add(new ArrayList<String>()); // # Calificación
        listaKardex.get(0).get(0).add("");
        listaKardex.get(1).get(0).add("");
        listaKardex.get(2).get(0).add("No hay información para mostrar.\n\nRazones:\n\n" +
                " - Aún vas en primer semestre.\n\n" +
                " - Te diste de baja antes de completar un semestre.");
        listaKardex.get(3).get(0).add("");
        listaKardex.get(4).get(0).add("");
        listaKardex.get(5).get(0).add("");

        if(saesKardex.select("span#ctl00_mainCopy_Lbl_Kardex").hasText()){
            listaKardex.get(0).clear();
            listaKardex.get(1).clear();
            listaKardex.get(2).clear();
            listaKardex.get(3).clear();
            listaKardex.get(4).clear();
            listaKardex.get(5).clear();

            span = saesKardex.select("span#ctl00_mainCopy_Lbl_Kardex").first();
            semestres = span.select("center").size();

            for(int i = 0; i < semestres; i++){
                listaKardex.get(0).add(new ArrayList<String>());
                listaKardex.get(1).add(new ArrayList<String>());
                listaKardex.get(2).add(new ArrayList<String>());
                listaKardex.get(3).add(new ArrayList<String>());
                listaKardex.get(4).add(new ArrayList<String>());
                listaKardex.get(5).add(new ArrayList<String>());
            }

            for(int f = 0; f < semestres; f++){
                center = span.select("center").get(f);
                table = center.select("table").first();
                tbody = table.select("tbody").first();
                td = tbody.select("td").iterator();

                listaSemestre.add(td.next().text());

                int i = 0;
                while(i < 6){
                    td.next();
                    i++;
                }
                i = 0;
                while(td.hasNext()){
                    if(i == 6) i = 0;
                    listaKardex.get(i).get(f).add(td.next().text());
                    i++;
                }
            }
        }
        if(saesKardex.select("span#ctl00_mainCopy_Lbl_Carrera").hasText()){
            carrera = saesKardex.select("span#ctl00_mainCopy_Lbl_Carrera").first().text();
        }
        if(saesKardex.select("span#ctl00_mainCopy_Lbl_Plan").hasText()){
            plan = saesKardex.select("span#ctl00_mainCopy_Lbl_Plan").first().text();
        }
        if(saesKardex.select("span#ctl00_mainCopy_Lbl_Promedio").hasText()){
            promedio = saesKardex.select("span#ctl00_mainCopy_Lbl_Promedio").first().text();
            Reinscripcion.setPromedioGen(promedio);
        }
        if(saesKardex.select("div#banner").hasText()){
            escuela = saesKardex.select("div#banner").first().text();
        }

        Alumno.setAlumno(escuela, carrera, plan, promedio);
        Log.d(TAG, "setKárdex ~ Terminado. #");
    }

    private void getEstado(){
        Element table, tbody;
        Iterator<Element> td;

        Log.d(TAG, "Obtener estado del alumno.");
        /* Reprobadas */
        /* Inicialización */
        Reinscripcion.setDesfasadas(0);
        Reinscripcion.setReprobadas(0);
        Reinscripcion.setNocursadas(0);

        if(saesAcademico.select("table#ctl00_mainCopy_grvEstatus_alumno").hasText()){
            table = saesAcademico.select("table#ctl00_mainCopy_grvEstatus_alumno").first();
            tbody = table.select("tbody").first();
            td = tbody.select("td").iterator();
            listaAcademico.get(0).clear();
            listaAcademico.get(1).clear();

            while(td.hasNext()){
                listaAcademico.get(0).add(td.next().text());
                listaAcademico.get(1).add(td.next().text());
            }

            Reinscripcion.setReprobadas(listaAcademico.get(0).size());
        }

        //------------ DESFASADAS ------------ #
        if(saesAcademico.select("table#ctl00_mainCopy_GridView2").hasText()){
            table = saesAcademico.select("table#ctl00_mainCopy_GridView2").first();
            tbody = table.select("tbody").first();
            td = tbody.select("td").iterator();

            listaAcademico.get(2).clear();
            listaAcademico.get(3).clear();

            while(td.hasNext()){
                listaAcademico.get(2).add(td.next().text());
                listaAcademico.get(3).add(td.next().text());
            }
            Reinscripcion.setDesfasadas(listaAcademico.get(2).size());
        }

        //------------ NO CURSADAS ------------ #
        if(saesAcademico.select("table#ctl00_mainCopy_GridView1").hasText()){
            table = saesAcademico.select("table#ctl00_mainCopy_GridView1").first();
            tbody = table.select("tbody").first();
            td = tbody.select("td").iterator();

            listaAcademico.get(4).clear();
            listaAcademico.get(5).clear();

            while(td.hasNext()){
                listaAcademico.get(4).add(td.next().text());
                listaAcademico.get(5).add(td.next().text());
            }
            Reinscripcion.setNocursadas(listaAcademico.get(4).size());
        }

        System.out.println(" # Academico list: ");
        System.out.println(listaAcademico);
    }

    private void getHorario(){
        Log.d(TAG, "Obtener horario del alumno.");
        Element tbody;
        Iterator<Element> td;

        if (saesHorario.select("table#ctl00_mainCopy_GV_Horario").hasText()) {
            listaHorario.clear();

            Element table = saesHorario.select("table#ctl00_mainCopy_GV_Horario").first();
            tbody = table.select("tbody").first();
            td = tbody.select("td").iterator();

            while(td.hasNext()){
                listaHorario.add(new MateriaHorario(td.next().text(), td.next().text(),
                        td.next().text(), "EDIFICIO: "+td.next().text(),
                        "SALÓN: "+td.next().text(), td.next().text(), td.next().text(),
                        td.next().text(),td.next().text(),td.next().text(),td.next().text()));
            }
        }
        if(listaHorario.get(0).getGrupo().equals(" ")) {
            listaHorario.get(0).setNombre("No hay información para mostrar.\n\nRazones:\n\n" +
                    " - No estás inscrito.\n\n" +
                    " - El periodo ordinario ha finalizado.\n\n - Estás dado de baja.");
        }

    }

    private void getCita(){
        Log.d(TAG, "Obtener cita de reinscripción.");
        Element tbody;
        Iterator<Element> td;

        // ---------------- CITA DE REINSCRIPCIÓN -----------------
        if(saesCita == null) return;
        if (saesCita.select("table#ctl00_mainCopy_grvEstatus_alumno").hasText()) {
            Element table = saesCita.select("table#ctl00_mainCopy_grvEstatus_alumno").first();
            tbody = table.select("tbody").first();
            td = tbody.select("td").iterator();
            td.next();
            td.next();
            listaCita.get(0).clear();

            while(td.hasNext()){
                listaCita.get(0).add(td.next().text());
            }
        }

        // ---------------- INFORMACIÓN SOBRE TU CARRERA ----------------
        if (saesCita.select("table#ctl00_mainCopy_CREDITOSCARRERA").hasText()) {
            Element table = saesCita.select("table#ctl00_mainCopy_CREDITOSCARRERA").first();
            tbody = table.select("tbody").first();
            td = tbody.select("td").iterator();
            listaCita.get(1).clear();

            while(td.hasNext()){
                listaCita.get(1).add(td.next().text());
            }
        }

        // ------ INFORMACIÓN SOBRE TU TRAYECTORIA ESCOLAR EN CRÉDITOS ------
        if (saesCita.select("table#ctl00_mainCopy_alumno").hasText()) {
            Element table = saesCita.select("table#ctl00_mainCopy_alumno").first();
            tbody = table.select("tbody").first();
            td = tbody.select("td").iterator();
            listaCita.get(2).clear();

            while(td.hasNext()){
                listaCita.get(2).add(td.next().text());
            }
        }
    }

    private void getETS(){
        Log.d(TAG, "Obtener examenes ETS.");
        Element tbody;
        Iterator<Element> td;
        int datos = 0, itera = 0;

        String[] periodo = { "" };
        String[] tipo = { "" };
        String[] clave = { "" };
        String[] materia = { "No hay información para mostrar.\n\nRazones:\n\n" +
                " - No es periodo de ETS.\n\n" +
                " - No tienes ETS inscritos." };
        String[] turno = { "" };
        String[] calif = { "" };

        if (saesETS.select("table#ctl00_mainCopy_GridView1").hasText()) {
            Element table = saesETS.select("table#ctl00_mainCopy_GridView1").first();
            tbody = table.select("tbody").first();
            td = tbody.select("td").iterator();

            while(td.hasNext()){
                td.next();
                datos++;
            }

            periodo = new String[datos/6];
            tipo = new String[datos/6];
            clave = new String[datos/6];
            materia = new String[datos/6];
            turno = new String[datos/6];
            calif = new String[datos/6];

            td = tbody.select("td").iterator();

            while(td.hasNext()){
                periodo[itera] = "PERIODO: "+td.next().text();
                tipo[itera] = "TIPO ETS: "+td.next().text().toUpperCase();
                clave[itera] = td.next().text().toUpperCase();
                materia[itera] = td.next().text();
                turno[itera] = "TURNO: "+td.next().text();
                calif[itera] = td.next().text();
                itera++;
            }
        }
        ETS.setETS(periodo, tipo, clave, materia, turno, calif);
    }

    private void wipeLists(){
        // # Método para inicializar las listas
        listaKardex = null;
        listaKardex = new ArrayList<>();
    }

    /* Cookies */
    private boolean checkCookie(String url){
        saesCookie = getCookie(url);
        Boolean cookieValid = false;
        if (saesCookie != null){
            cookieValid = false;
            if (!saesCookie.isEmpty()){
                cookieValid = saesCookie.toUpperCase().contains("ASPXFORMSAUTH");
            }
        }
        Log.d(TAG, "Cookies verificadas. ["+cookieValid+"]");
        return cookieValid;
    }

    private String getCookie(String url) {
        String str = "";
        if (url != null)
            if (cookieManager.hasCookies()) {
                str = cookieManager.getCookie(url);
            }
        Log.d(TAG, "Cookies obtenidas.");
        return str;
    }

    @SuppressWarnings("deprecation")
    private void wipeCookie(String url){
        saesCookie = getCookie(url);
        cookieSyncMngr.startSync();
        cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        cookieSyncMngr.stopSync();
        cookieSyncMngr.sync();
        Log.d(TAG, "Cookies limpiadas.");
    }

    // Funciones setters y getters
    // Horario
    public List<MateriaHorario> getListHorario(){
        return listaHorario;
    }
    /*
    public void setHorario(List<List<String>> pack){
        listaHorario = pack;
        //Log.d(TAG, "listaHorario: "+listaHorario);
    }
    */

    // Estado general
    public List<List<String>> getListAcademico(){
        return listaAcademico;
    }
    /*
    public void setAcademico(List<List<String>> pack){
        listaAcademico = pack;
        //Log.d(TAG, "listaAcademico: "+listaAcademico);
    }
    */

    // Calificaciones del semestre
    public List<MateriaCalificaciones> getListOrdinario(){
        return listaOrdinario;
    }
    /*
    public void setOrdinario(List<List<String>> pack){
        listaOrdinario = pack;
        //Log.d(TAG, "listaOrdinario: "+listaOrdinario);
    }
    */

    // Kárdex
    public List<List<List<String>>> getListKardex(){
        return listaKardex;
    }
    public List<String> getListSemestre(){ return listaSemestre; }
    /*
    public void setKardex(List<List<List<String>>> pack, List<String> semestres, int i){
        listaKardex.add(pack);
        listaKardex.add(new ArrayList<List<List<String>>>());
        listaKardex.get(1).add(new ArrayList<List<String>>());
        listaKardex.get(1).get(0).add(semestres);
        listaKardex.get(1).add(new ArrayList<List<String>>());
        listaKardex.get(1).get(1).add(new ArrayList<String>());
        listaKardex.get(1).get(1).get(0).add(String.valueOf(i));
        //Log.d(TAG, "listaKárdex: "+listaKardex);
    }
    */

    // Cita de reinscripción
    public List<List<String>> getListaCita(){
        return listaCita;
    }
    /*
    public void setCita(List<List<String>> pack){
        listaCita = pack;
        //Log.d(TAG, "listaCita: "+listaCita);
    }
    */
}
