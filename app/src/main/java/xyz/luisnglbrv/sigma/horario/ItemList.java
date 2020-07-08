package xyz.luisnglbrv.sigma.horario;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xyz.luisnglbrv.sigma.Preferencias;
import xyz.luisnglbrv.sigma.R;

/**
 * Created by LuisAngel on 15/12/16.
 */

public class ItemList extends ArrayAdapter<String> {

    private final Activity context;
    private List<List<String>> listaHorario;
    private List<String> listaDia;
    private final String hoy;
    private int diaHoy = 0;
    private Preferencias datos;
    private Locale loc;

    public ItemList(Activity context, String hoy, List<List<String>> pack, List<String> dia) {
        super(context, R.layout.item_list_horario, dia);
        this.hoy = hoy;
        this.context = context;
        this.listaHorario = pack;
        this.listaDia = dia;
    }

    private class ViewHolder{
        private TextView txtMateria;
        private TextView txtProfesor;
        private TextView txtEdificio;
        private TextView txtSalon;
        private TextView txtHoras;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        datos = new Preferencias(getContext());
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder h;

        if(view == null){
            view = inflater.inflate(R.layout.item_list_horario, parent, false);
            h = new ViewHolder();

            h.txtMateria = (TextView) view.findViewById(R.id.horario_txtMateria);
            h.txtProfesor = (TextView) view.findViewById(R.id.horario_txtProfe);
            h.txtEdificio = (TextView) view.findViewById(R.id.horario_txtEdificio);
            h.txtSalon = (TextView) view.findViewById(R.id.horario_txtSalon);
            h.txtHoras = (TextView) view.findViewById(R.id.horario_txtHoras);

            view.setTag(h);
        }else{
            h = (ViewHolder) view.getTag();
        }

        switch (hoy) {
            case "LUNES":
                diaHoy = 2;
                break;
            case "MARTES":
                diaHoy = 3;
                break;
            case "MIÉRCOLES":
                diaHoy = 4;
                break;
            case "JUEVES":
                diaHoy = 5;
                break;
            case "VIERNES":
                diaHoy = 6;
                break;
            case "SÁBADO":
                diaHoy = 7;
                break;
        }

        if(diaHoy != 0) {
            loc = new Locale("es", "MX");
            Calendar c = Calendar.getInstance();
            String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.valueOf(c.get(Calendar.MINUTE));
            int day = dayOfWeek(getDate());
            Date dateYet = new Date(), dateMateria = new Date(), futureDate = new Date();
            try {
                dateYet = new SimpleDateFormat("HH:mm", loc).parse(hour);
                if (listaDia.get(position).length() != 0) {
                    String horas[] = listaDia.get(position).split("-");
                    dateMateria = new SimpleDateFormat("HH:mm", loc).parse(horas[0].trim());
                    futureDate = new SimpleDateFormat("HH:mm", loc).parse(horas[1].trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (day == diaHoy) {
                if (dateYet.after(dateMateria) && dateYet.before(futureDate)) {
                    h.txtMateria.setTextColor(datos.getColorSelected());
                    h.txtProfesor.setTextColor(datos.getColorSelected());
                    h.txtEdificio.setTextColor(datos.getColorSelected());
                    h.txtSalon.setTextColor(datos.getColorSelected());
                    h.txtHoras.setTextColor(datos.getColorSelected());
                }
            }

        }

        h.txtMateria.setText(listaHorario.get(1).get(position));
        h.txtProfesor.setText(listaHorario.get(0).get(position) + "  •  " +listaHorario.get(2).get(position));
        h.txtEdificio.setText(listaHorario.get(3).get(position));
        h.txtSalon.setText(listaHorario.get(4).get(position));
        h.txtHoras.setText(listaDia.get(position));

        if(listaHorario.get(1).get(0).equals("No hay información para mostrar.\n\nRazones:\n\n" +
                " - No estás inscrito.\n\n" +
                " - El periodo ordinario ha finalizado.\n\n - Estás dado de baja.")){
            h.txtProfesor.setVisibility(View.GONE);
            h.txtEdificio.setVisibility(View.GONE);
            h.txtSalon.setVisibility(View.GONE);
        }

        view.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#FAFAFA") :
                Color.parseColor("#F5F5F5"));
        return view;
    }

    public String getDate() {
        loc = new Locale("es", "MX");
        Date myDate = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd", loc).format(myDate);
        // new SimpleDateFormat("hh-mm-a").format(myDate);
        return date;
    }

    private int dayOfWeek(String date) {
        try {
            int day = 0;
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy-MM-dd", loc);
            Date now = simpleDateformat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            day = calendar.get(Calendar.DAY_OF_WEEK);
            return day;
        } catch (Exception e) {
            return -1;
        }
    }
}
