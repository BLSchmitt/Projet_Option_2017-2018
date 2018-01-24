import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Probleme {
	
	public int NombreDeVariables;  //fait
	public double MoyenneTaillesDomaine;//fait
	public double EcratTypeDesDomaines;//fait
	public double TailleDuDomaineCommun; // taux de repetition fait
	public double MoyenneDuNombreDeVariableParClause;//fait
	public double EcartTypeNbrVariablesParClause;//fait
	//public double MoyennePourcentageRestrictionDom;
	//public double EcartTypePourcentageRestrictionDom;
	public double MoyennePourcentageDesVariablesImpacteesClauses;//fait
	//reste param sur le comportement primaire des clauses ! voir visualisations Michal.




	public Probleme( int nombreDeVariables, double moyenneTaillesDomaine, double ecratTypeDesDomaines,
			double tailleDuDomaineCommun, double moyenneDuNombreDeVariableParClause,
			double ecartTypeNbrVariablesParClause, 
			double MoyennepourcentageDesVariablesImpacteesClauses) {
		super();
	
		NombreDeVariables = nombreDeVariables;
		MoyenneTaillesDomaine = moyenneTaillesDomaine;
		EcratTypeDesDomaines = ecratTypeDesDomaines;
		TailleDuDomaineCommun = tailleDuDomaineCommun;
		MoyenneDuNombreDeVariableParClause = moyenneDuNombreDeVariableParClause;
		EcartTypeNbrVariablesParClause = ecartTypeNbrVariablesParClause;

		MoyennePourcentageDesVariablesImpacteesClauses = MoyennepourcentageDesVariablesImpacteesClauses;
	}


	public int getNombreDeVariables() {
		return NombreDeVariables;
	}


	public void setNombreDeVariables(int nombreDeVariables) {
		NombreDeVariables = nombreDeVariables;
	}


	public double getMoyenneTaillesDomaine() {
		return MoyenneTaillesDomaine;
	}


	public void setMoyenneTaillesDomaine(double moyenneTaillesDomaine) {
		MoyenneTaillesDomaine = moyenneTaillesDomaine;
	}


	public double getEcratTypeDesDomaines() {
		return EcratTypeDesDomaines;
	}


	public void setEcratTypeDesDomaines(double ecratTypeDesDomaines) {
		EcratTypeDesDomaines = ecratTypeDesDomaines;
	}


	public double getTailleDuDomaineCommun() {
		return TailleDuDomaineCommun;
	}


	public void setTailleDuDomaineCommun(double tailleDuDomaineCommun) {
		TailleDuDomaineCommun = tailleDuDomaineCommun;
	}


	public double getMoyenneDuNombreDeVariableParClause() {
		return MoyenneDuNombreDeVariableParClause;
	}


	public void setMoyenneDuNombreDeVariableParClause(double moyenneDuNombreDeVariableParClause) {
		MoyenneDuNombreDeVariableParClause = moyenneDuNombreDeVariableParClause;
	}


	public double getEcartTypeNbrVariablesParClause() {
		return EcartTypeNbrVariablesParClause;
	}


	

	public void setEcartTypeNbrVariablesParClause(double ecartTypeNbrVariablesParClause) {
		EcartTypeNbrVariablesParClause = ecartTypeNbrVariablesParClause;
	}


	public double getPourcentageDesVariablesImpacteesClauses() {
		return MoyennePourcentageDesVariablesImpacteesClauses;
	}


	public void setPourcentageDesVariablesImpacteesClauses(double MoyennepourcentageDesVariablesImpacteesClauses) {
		MoyennePourcentageDesVariablesImpacteesClauses =MoyennepourcentageDesVariablesImpacteesClauses;
	}

	public static Probleme generateProbFromFile (String urldom , String urlnogood) throws FileNotFoundException, IOException, ParseException {




		ArrayList<String> Variables = new ArrayList<String>();
		ArrayList<JSONArray> Domaines = new ArrayList<JSONArray>();


		// parser un fichier DOM en JSON  : 
		JSONParser parser = new JSONParser();


		Object obj = parser.parse(new FileReader(urldom));
		JSONArray jsonarray = (JSONArray) obj;

		for (int i=0; i< jsonarray.size();i++){
			JSONObject j = (JSONObject) jsonarray.get(i);
			String variable = (String) j.get("var");
			Variables.add(variable);

			JSONArray Domaine = new JSONArray();
			Domaine = (JSONArray) j.get("in");
			Domaines.add(Domaine);

		}


		// Calcul de param et création du Problème à partir de fichier dom: 

		// Nombre de Variables 
		int nbrvar = Variables.size();
		// Taille moyenne de domaines 

		int somme =0; 

		for (int i1=0; i1<Domaines.size(); i1++){
			somme = somme + Domaines.get(i1).size();

		}
		double moy = somme/Domaines.size();
		// ecart type
		double somme2 =0.0;
		for (int i1=0;i1< Domaines.size(); i1++){
			somme2 =somme2 + ((Domaines.get(i1).size()- moy)*(Domaines.get(i1).size()- moy));
		}
		double EcartType = Math.sqrt(somme2/Domaines.size());

		// Calcul de param et création du Problème à partir de fichier nogood: 

		JSONParser parserng = new JSONParser();
		Object objng = parserng.parse(new FileReader(urlnogood));
		JSONArray jsonarrayng = (JSONArray) objng;


		// nombre de variable moyen par clause 

		int sommeng=0;
		for (int i=0 ; i< jsonarrayng.size();i++) {
			JSONArray js = (JSONArray) jsonarrayng.get(i);
			somme = somme+ js.size();
		}
		double moyParClause = somme/jsonarrayng.size();


		// écart type du nombre de variable par clause
		double sommeEType=0;
		for (int i=0 ; i< jsonarrayng.size();i++) {
			JSONArray js = (JSONArray) jsonarrayng.get(i);
			sommeEType = sommeEType+ (Math.abs(js.size()-moyParClause));
		}
		double EcartTypeNbVarParClause = sommeEType/ jsonarrayng.size();

		//moyenne du pourcentage des variables impactées par clause : 
		float s=0;
		for (int i=0 ; i< jsonarrayng.size();i++) {
			JSONArray js = (JSONArray) jsonarrayng.get(i);
			s= (float) (s+(js.size()*100/Variables.size()));	

		}

		double moyPourVarImpacClause = s/jsonarrayng.size();


		// Taux de répétition: 
		ArrayList<Long> repetition = new ArrayList<Long>();
		for (int i=0 ; i< jsonarrayng.size();i++) {
			JSONArray js = (JSONArray) jsonarrayng.get(i);
			for (int k=0; k< js.size(); k++) {
				JSONObject b = (JSONObject) js.get(k);
				JSONArray notin = (JSONArray) b.get("notin");
				for (int n=0;n<notin.size();n++) {


					repetition.add((Long) notin.get(n));
				}
			}

		}

		Collections.sort(repetition,Collections.reverseOrder());
		System.out.println(repetition);
		double tauxDeRep = (repetition.size()/repetition.get(0));


		// Création de Problème : 


		return new Probleme(nbrvar,moy,EcartType,tauxDeRep,moyParClause,EcartTypeNbVarParClause,moyPourVarImpacClause);

	}



	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

		
		
		
		
	


		File folder = new File("C:\\Users\\easyultrasus\\Desktop\\DATA Projet d'option");
		File[] listOfFiles = folder.listFiles();
		
		
// ------------------------------------ TRAITEMENT SYNTAXIQUE DES FICHIERS--------------------------------------------------------------------		

		for (File file : listOfFiles) {
			if (file.isFile() && file.length()!=0) {
				RandomAccessFile f = new RandomAccessFile(new File(file.getPath()), "rw");
				f.seek(f.length()-1);
				f.write(" ".getBytes());
				f.close();
	
				System.out.println(file.getPath());
				
			}
		}

//		int compteur = 0;
//		for (int i =0 ; i< listOfFiles.length/2; i++) {
//		
//			if (listOfFiles[2*i].isFile() && listOfFiles[2*i].length()!=0 ) {
//				RandomAccessFile f = new RandomAccessFile(new File(listOfFiles[2*i].getPath()), "rw");
//				f.seek(0); // to the beginning
//				f.write("[".getBytes());
//				f.seek(f.length()-1);
//				f.write("]".getBytes());
//				f.close();
//			}
//			
//				System.out.println(listOfFiles[2*i].getPath());
//			
//		}


		//--------------------------------------------------------------------------------------------------------------------------
/*
		
		ArrayList<Probleme> Problemes = new ArrayList<Probleme>();
		for (int i =0 ; i< listOfFiles.length/2; i++) {
			System.out.println(i);
			if (listOfFiles[2*i].length()!=0 && listOfFiles[2*i+1].length()!=0) {
			Problemes.add(generateProbFromFile(listOfFiles[2*i].getPath(),listOfFiles[2*i+1].getPath()));
			
			}
			}
*/



	}	

}












