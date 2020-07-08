package xyz.luisnglbrv.sigma.escuelas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import xyz.luisnglbrv.sigma.LoginSaes;
import xyz.luisnglbrv.sigma.Preferencias;
import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 21/12/16.
 */

public class EscuelaTabFragment extends Fragment {

    private Preferencias datos;

    int contador;

    public static final String TEXT = "text";

    public static Fragment newInstance(String text) {
        EscuelaTabFragment f = new EscuelaTabFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.escuela_tab_fragment, container, false);

        datos = new Preferencias(this.getContext());
        GridView gridView = (GridView) view.findViewById(R.id.gridView);

        if(getArguments().getString(TEXT).equals("MEDIO SUPERIOR")) {
            AdaptadorMedio adaptador = new AdaptadorMedio(getContext());
            gridView.setAdapter(adaptador);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        Toast.makeText(getContext(), "CECyT 1 \"Gonzalo Vázquez Vela\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt1.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 1");
                    } else if (position == 1) {
                        Toast.makeText(getContext(), "CECyT 2 \"Miguel Bernard Perales\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt2.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 2");
                    } else if (position == 2) {
                        Toast.makeText(getContext(), "CECyT 3 \"Estanislao Ramírez Ruiz\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt3.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 3");
                    } else if (position == 3) {
                        Toast.makeText(getContext(), "CECyT 4 \"Lázaro Cárdenas\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt4.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 4");
                    } else if (position == 4) {
                        Toast.makeText(getContext(), "CECyT 5 \"Benito Juárez\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt5.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 5");
                    } else if (position == 5) {
                        Toast.makeText(getContext(), "CECyT 6 \"Miguel Othón de Mendizabal\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt6.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 6");
                    } else if (position == 6) {
                        Toast.makeText(getContext(), "CECyT 7 \"Cuauhtemoc\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt7.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 7");
                    } else if (position == 7) {
                        Toast.makeText(getContext(), "CECyT 8 \"Narciso Bassols García\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt8.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 8");
                    } else if (position == 8) {
                        Toast.makeText(getContext(), "CECyT 9 \"Juan de Dios Bátiz\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt9.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 9");
                    } else if (position == 9) {
                        Toast.makeText(getContext(), " CECyT 10 \"Carlos Vallejo Márquez\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt10.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 10");
                    } else if (position == 10) {
                        Toast.makeText(getContext(), "CECyT 11 \"Wilfrido Massieu\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt11.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 11");
                    } else if (position == 11) {
                        Toast.makeText(getContext(), "CECyT 12 \"José María Morelos\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt12.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 12");
                    } else if (position == 12) {
                        Toast.makeText(getContext(), "CECyT 13 \"Ricardo Flores Magón\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt13.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 13");
                    } else if (position == 13) {
                        Toast.makeText(getContext(), "CECyT 14 \"Luis Enrique Erro Soler\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt14.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 14");
                    } else if (position == 14) {
                        Toast.makeText(getContext(), "CECyT 15 \"Diodoro Antúnez Echegaray\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt15.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 15");
                    } else if (position == 15) {
                        Toast.makeText(getContext(), "CECyT 16 \"Hidalgo\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt16.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 16");
                    } else if (position == 16) {
                        Toast.makeText(getContext(), "CECyT 17 \"León, Guanajuato\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://148.204.250.26");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 17");
                    } else if (position == 17) {
                        Toast.makeText(getContext(), "CECyT 18 \"Zacatecas\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cecyt18.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CECyT 18");
                    } else if (position == 18) {
                        Toast.makeText(getContext(), "CET 1 \"Walter Cross Buchanan\"", Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cet1.ipn.mx");
                        datos.setIdDraw(0, position);
                        datos.setEscuela("CET 1");
                    }
                    startActivity(new Intent(getActivity(), LoginSaes.class));
                    getActivity().finish();
                }
            });
        }else if(getArguments().getString(TEXT).equals("SUPERIOR")){
            AdaptadorSuperior adaptador = new AdaptadorSuperior(getContext());
            gridView.setAdapter(adaptador);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        Toast.makeText(getContext(), "CICS Unidad Milpa Alta",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cicsma.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("CICS Milpa Alta");
                    } else if (position == 1) {
                        Toast.makeText(getContext(), "CICS Unidad Santo Tomás",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.cicsst.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("CICS Santo Tomás");
                    } else if (position == 2) {
                        Toast.makeText(getContext(), "Escuela Nacional de Ciencias Biológicas",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.encb.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ENCB");
                    } else if (position == 3) {
                        Toast.makeText(getContext(), "Escuela Nacional de Medicina y Homeopatía",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.enmh.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ENMH");
                    } else if (position == 4) {
                        Toast.makeText(getContext(), "ESCA Unidad Santo Tomás",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.escasto.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESCA Santo Tomás");
                    } else if (position == 5) {
                        Toast.makeText(getContext(), "ESCA Unidad Tepepan",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.escatep.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESCA Tepepan");
                    } else if (position == 6) {
                        Toast.makeText(getContext(), "Escuela Superior de Cómputo",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.escom.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESCOM");
                    } else if (position == 7) {
                        Toast.makeText(getContext(), "Escuela Superior de Economía",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.ese.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESE");
                    } else if (position == 8) {
                        Toast.makeText(getContext(), "Escuela Superior de Enfermería y Obstetricia",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.eseo.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESEO");
                    } else if (position == 9) {
                        Toast.makeText(getContext(), "Escuela Superior de Física y Matemáticas",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esfm.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESFM");
                    } else if (position == 10) {
                        Toast.makeText(getContext(), "ESIA Unidad Tecamachalco",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esiatec.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESIA Tecamachalco");
                    } else if (position == 11) {
                        Toast.makeText(getContext(), "ESIA Unidad Ticomán",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esiatic.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESIA Ticomán");
                    } else if (position == 12) {
                        Toast.makeText(getContext(), "ESIA Zacatenco",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esiaz.ipn.mx/");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESIA Zacatenco");
                    } else if (position == 13) {
                        Toast.makeText(getContext(), "ESIME Azcapotzalco",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esimeazc.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESIME Azcapotzalco");
                    } else if (position == 14) {
                        Toast.makeText(getContext(), "ESIME Culhuacán",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esimecu.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESIME Culhuacán");
                    } else if (position == 15) {
                        Toast.makeText(getContext(), "ESIME Ticomán",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esimetic.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESIME Ticomán");
                    } else if (position == 16) {
                        Toast.makeText(getContext(), "ESIME Zacatenco",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esimez.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESIME Zacatenco");
                    } else if (position == 17) {
                        Toast.makeText(getContext(),
                                "Escuela Superior de Ingeniería Química e Industrias Extractivas",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esiqie.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESIQIE");
                    } else if (position == 18) {
                        Toast.makeText(getContext(), "Escuela Superior de Ingeniería Textil",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esit.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESIT");
                    } else if (position == 19) {
                        Toast.makeText(getContext(), "Escuela Superior de Medicina",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.esm.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("ESM");
                    }else if (position == 20) {
                        Toast.makeText(getContext(), "Escuela Superior de Turismo",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.est.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("EST");
                    } else if (position == 21) {
                        Toast.makeText(getContext(),
                                "Unidad Profesional Interdisciplinaria de Biotecnología",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.upibi.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("UPIBI");
                    } else if (position == 22) {
                        Toast.makeText(getContext(),
                                "Unidad Profesional Interdisciplinaria de Ingeniería y Ciencias Sociales y Administrativas",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.upiicsa.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("UPIICSA");
                    } else if (position == 23) {
                        Toast.makeText(getContext(),
                                "Unidad Profesional Interdisciplinaria De Ingeniería Campus Guanajuato",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.upiig.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("UPIIG");
                    } else if (position == 24) {
                        Toast.makeText(getContext(),
                                "Unidad Profesional Interdisciplinaria de Ingeniería Campus Hidalgo",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://148.204.250.36");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("UPIIH");
                    } else if (position == 25) {
                        Toast.makeText(getContext(),
                                "Unidad Profesional Interdisciplinaria en Ingeniería y Tecnologías Avanzadas",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.upiita.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("UPIITA");
                    } else if (position == 26) {
                        Toast.makeText(getContext(),
                                "Unidad Profesional Interdisciplinaria en Ingeniería Campus Zacatecas",
                                Toast.LENGTH_SHORT).show();
                        datos.setURL("https://www.saes.upiiz.ipn.mx");
                        datos.setIdDraw(1, position);
                        datos.setEscuela("UPIIZ");
                    }
                    startActivity(new Intent(getActivity(), LoginSaes.class));
                    getActivity().finish();
                }
            });
        }

        return view;
    }
}
