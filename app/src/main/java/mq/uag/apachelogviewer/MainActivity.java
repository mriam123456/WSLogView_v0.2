package mq.uag.apachelogviewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import java.util.List;

/**
 * Auteur et Enseignement : Manuel RIAM
 *
 * POINT D'ENTREE GLOBAL DE L'APPLICATION
 * https://developer.android.com/reference/android/app/Activity.html
 * ======================================
 * TD - Construction Applicative sous Android Etape 2 (java103 / android 102)
 * ---------------------------------------------------
 * Fonctions expliquée en cours :
 * - View findViewById(int id) - https://developer.android.com/reference/android/app/Activity.html#findViewById(int)
 *      *cast du type de retour
 * - Contexte / passage du contexte et conséquences
 * - Type ContentValues utilisation avec boucle
 * - checkup SQL
 * - Cursor Traversal avec do{}while() et itérator du Cursor (moveFirst()/moveNext()/cursor.get*())
 *
 * =========================
 * Objectifs pédagogique : ||
 * =========================
 * - Découverte API IHM d'Android
 * - Encacapsulation de fonctionnalité (Facilite les portages)
 * - Découverte Design Pattern Adapter
 * - Découverte couche SQLite
 * - Consolidation bases programmation sur collections
 * - Découverte du Cursor
 */
public class MainActivity extends AppCompatActivity {


    private Context _c;

    //Liste de LogEntry
    public List<LogEntry> _activeViewList ;

    // Liste extensible utilisée comme Frond-End pour l'affichage des Logs
    private ExpandableListView elv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _c = this;

        //Association du layout activity_main à notre MainActivity
        setContentView(R.layout.activity_main);

        //init couche SQLite
        LogManager.initDb(this);

        //Chargement de la liste de logEntry depuis la BDD
        _activeViewList = LogManager.loadBaseList();

        //Récupère la View correspondant à mainLV et la cast en ExpandableListView dans elv;
        elv = (ExpandableListView) findViewById(R.id.mainLV);


        //Associe un adapter (SimpleExpandableListAdapter)
        elv.setAdapter(refreshView());
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                //=====A CODER====//
                return true;
            }
        });




    }

    /**
     * Fonction utilitaire (Dev // A noter : L'implémentation finale du programme n'utilisera pas cet adapter)
     * @return SimpleExpandableListAdapter (https://newfivefour.com/android-SimpleExpandableListAdapter-example.html)
     */
    private SimpleExpandableListAdapter refreshView()
    {
        //Instanciation et renvoi d'un nouvel SimpleExpandableListAdapter , entièrement paramètré pour
        //le cas de base
        return new SimpleExpandableListAdapter(
                this,//paramètre 1 ._. Context , en l'occurence notre MainActivity
                LogManager.getTopLevelListOfMaps(_activeViewList),//paramètre 2 ._. List de Map . Chaque Map représente une ligne dans la liste <clé,valeur>
                R.layout.parent_item_log_entry,//paramètre 3 ._.  Identifiant pour Layout XML élément parent (android.R. ou R.)
                new String[]{
                        LogReaderContract.ALEntry.COLUMN_NAME_REMOTEHOST , //Nom Clé en DB (voir classe LogReaderContract)
                        LogReaderContract.ALEntry.COLUMN_NAME_CONTENTLENGTH, //Nom Clé en DB (voir classe LogReaderContract)
                        LogReaderContract.ALEntry.COLUMN_NAME_DATE //Nom Clé en DB (voir classe LogReaderContract)
                },//paramètre 4 ._. +++++>>> Tableau de clés(String) qui seront mappées aux identifiants(int) de goupTo <<<+++
                new int[] {
                        R.id.parentTV_RemoteHost, // > Référence élément TextView dans res.layout.parent_item_log_entry
                        R.id.parentTV_cLength, // // > Référence élément TextView dans res.layout.parent_item_log_entry
                        R.id.parentTV_datetime // > Référence élément TextView dans res.layout.parent_item_log_entry
                },//paramètre 5 ._. +++++>>> Tableau d'identifiants d'éléments du layout de l'élément parent <<<+++

                LogManager.getChildListOfListOfMaps(_activeViewList),//paramètre 6 ._.  List de List de Map , le premier niveau de list est mappé aux éléments de la ligne extensible
                R.layout.child_items_log_entry,//paramètre 7 ._. Identifiant pour Layout XML élément enfant(android.R. ou R.)
                new String[]{
                        LogReaderContract.ALEntry.COLUMN_NAME_HOST, //Nom Clé en DB (voir classe LogReaderContract)
                        LogReaderContract.ALEntry.COLUMN_NAME_URL, //Nom Clé en DB (voir classe LogReaderContract)
                        LogReaderContract.ALEntry.COLUMN_NAME_HTTPCODE //Nom Clé en DB (voir classe LogReaderContract)
                },//paramètre 6 ._. +++++>>> Tableau de clés(String) qui seront mappées aux identifiants(int) de childTo <<<+++
                new int[]{
                        R.id.childTV_host, // > Référence élément TextView dans res.layout.child_items_log_entry
                        R.id.childTV_url, // > Référence élément TextView dans res.layout.child_items_log_entry
                        R.id.childTV_code // > Référence élément TextView dans res.layout.child_items_log_entry
                }//paramètre 7 ._. +++++>>> Tableau d'identifiants d'éléments du layout de l'élément enfant <<<+++
        );
    }
}


