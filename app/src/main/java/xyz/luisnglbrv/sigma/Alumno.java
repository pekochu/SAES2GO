package xyz.luisnglbrv.sigma;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Fragmento principal
 */
public class Alumno extends Fragment {

    public static final String ARG_SECTION_TITLE = "section_number";
    private String nBoleta = "N/D";
    private String alumno = "N/D";
    private static String plantel;
    private static String carrera;
    private static String plan;
    private static String promedio;
    private Preferencias datos;
    private Context parentContext;

    public static Alumno newInstance(String sectionTitle) {
        Alumno fragment = new Alumno();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setAlumno(String txt, String txt1, String txt2, String txt3) {
        plantel = txt;
        carrera = txt1;
        plan = txt2;
        promedio = txt3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alumno, container, false);

        datos = new Preferencias(this.getContext());

        if(datos.getNombre() != null){ alumno = datos.getNombre(); }

        if(datos.getBoleta() != null){ nBoleta = datos.getBoleta(); }

        TextView txt, txt1, txt2, txt3, txt4, txt5;
        txt = (TextView) view.findViewById(R.id.textView);
        txt.setTextColor(datos.getColorSelected());
        txt1 = (TextView) view.findViewById(R.id.textView1);
        txt1.setTextColor(datos.getColorSelected());
        txt2 = (TextView) view.findViewById(R.id.textView2);
        txt2.setTextColor(datos.getColorSelected());
        txt3 = (TextView) view.findViewById(R.id.textView3);
        txt3.setTextColor(datos.getColorSelected());
        txt4 = (TextView) view.findViewById(R.id.textView4);
        txt4.setTextColor(datos.getColorSelected());
        txt5 = (TextView) view.findViewById(R.id.textView5);
        txt5.setTextColor(datos.getColorSelected());
        TextView escuela = (TextView) view.findViewById(R.id.txt_Escuela);
        TextView nombre = (TextView) view.findViewById(R.id.txt_Nombre);
        TextView boleta = (TextView) view.findViewById(R.id.txt_Boleta);
        TextView mCarrera = (TextView) view.findViewById(R.id.txt_Carrera);
        TextView mPlan = (TextView) view.findViewById(R.id.txt_Plan);
        TextView mPromedio = (TextView) view.findViewById(R.id.txt_Promedio);

        if(plantel.equals("")){
            startActivity(new Intent(parentContext, LoginSaes.class));
            if(getActivity() != null)
                getActivity().finish();
        }

        nombre.setText(alumno);
        escuela.setText(plantel);
        boleta.setText(nBoleta);
        mCarrera.setText(carrera);
        mPlan.setText(plan);
        mPromedio.setText(promedio);

        return view;
    }

    @Override
    public void onAttach(Context cnt){
        super.onAttach(cnt);
        parentContext = cnt;
    }

}


