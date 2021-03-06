
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import FitnessTracker.*;
import dataStructures.Iterator;
import exceptions.*;

/**
 * @author Pedro Xavier (47525) <p.xaviercampus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeiracampus.fct.unl.pt>
 */
public class Main {
	private static final String INSERIR_ATLETA = "IU";
	private static final String ALTERAR_INFORMACAO_ATLETA = "UU";
	private static final String REMOVE_ATLETA = "RU";
	private static final String CONSULTAR_DADOS_ATLETA = "CU";
	private static final String CRIAR_ATIVIDADE = "IA";
	private static final String ADICIONAR_TREINO = "AW";
	private static final String CONSULTAR_TREINO_ATLETA = "CW";
	private static final String ATUALIZAR_PASSOS = "AS";
	private static final String CRIAR_GRUPO_ATLETAS = "IG";
	private static final String ADESAO_ATLETA_GRUPO = "AG";
	private static final String DESISTENCIA_GRUPO = "DG";
	private static final String CONSULTA_GRUPO = "CG";
	private static final String LISTAR_CAMINHANTES = "LC";
	private static final String LISTAR_GUERREIROS = "LW";
	private static final String TERMINAR_EXECUCAO = "XS";
	
	private static final String DATA_FILE = "fileName.dat";

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		Scanner in = new Scanner(System.in);
		FitnessTracker ft = load();
		String comm = getCommand(in);

		while (!comm.equalsIgnoreCase(TERMINAR_EXECUCAO)) {
			switch (comm.toUpperCase()) {
			case INSERIR_ATLETA:
				insertAthlete(in, ft);
				break;
			case ALTERAR_INFORMACAO_ATLETA:
				changeAthleteData(in, ft);
				break;
			case REMOVE_ATLETA:
				removeAthlete(in, ft);
				break;
			case CONSULTAR_DADOS_ATLETA:
				athleteData(in, ft);
				break;
			case CRIAR_ATIVIDADE:
				insertActivity(in, ft);
				break;
			case ADICIONAR_TREINO:
				addTraining(in, ft);
				break;
			case CONSULTAR_TREINO_ATLETA:
				checkAthleteTraining(in, ft);
				break;
			case ATUALIZAR_PASSOS:
				upgradeSteps(in, ft);
				break;
			case CRIAR_GRUPO_ATLETAS:
				addGroup(in, ft);
				break;
			case ADESAO_ATLETA_GRUPO:
				addAthleteAtGroup(in, ft);
				break;
			case DESISTENCIA_GRUPO:
				removeAthleteAtGroup(in, ft);
				break;
			case CONSULTA_GRUPO:
				groupData(in, ft);
				break;
			case LISTAR_CAMINHANTES:
				listWalkers(ft);
				break;
			case LISTAR_GUERREIROS:
				listWarriors(ft);
				break;
			default:
				break;
			}
			comm = getCommand(in);
			System.out.println();
		}
		save(ft);
		System.out.println("Gravando e terminando...");
		System.out.println();
		//in.close();
		
	}

	private static void save(FitnessTracker ft) throws IOException{
		try{ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(DATA_FILE));
		file.writeObject(ft);
		file.flush();
		file.close();
		}
		catch(IOException e ){
			System.out.println();
		}
	}

	private static FitnessTracker load() throws FileNotFoundException, IOException, ClassNotFoundException {
		FitnessTracker ft = null;
		try{
		ObjectInputStream file = new ObjectInputStream(new FileInputStream(DATA_FILE) );
		ft =  (FitnessTracker) file.readObject();      
		file.close();
		}
		
		catch(ClassNotFoundException e){
			System.out.println(e.getMessage());
		}
		catch(IOException e){
			ft = new FitnessTrackerClass();
		}
		return ft;
	}

	private static String getCommand(Scanner in) {
		String input = in.next().trim();
		return input;
	}

	private static void insertAthlete(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().trim();
		int weigth = in.nextInt();
		int heigth = in.nextInt();
		int age = in.nextInt();
		String gender = in.next().trim();
		String name = in.nextLine().trim();

		try {
			ft.insertAthlete(idTracker, name, heigth, weigth, age, gender);
			System.out.println("Insercao de atleta com sucesso.");
		} catch (InvalidValueException e) {
			System.out.println(e.getMessage());
		} catch (AthleteAlreadyExistException e) {
			System.out.println((e.getMessage()));
		}

	}

	private static void changeAthleteData(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().trim();
		int weigth = in.nextInt();
		int heigth = in.nextInt();
		int age = in.nextInt();
		try {
			ft.changeAthleteData(idTracker, heigth, weigth, age);
			System.out.println("Atleta atualizado com sucesso.");
		} catch (InvalidValueException e) {
			System.out.println(e.getMessage());
		} catch (AthleteNotExistException e) {
			System.out.println(e.getMessage());
		}

	}

	private static void removeAthlete(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().trim();
		in.nextLine();
		try {
			ft.removeAthlete(idTracker);
			System.out.println("Atleta removido com sucesso.");
		} catch (AthleteNotExistException e) {
			System.out.println(e.getMessage());
		}

	}

	private static void athleteData(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().trim();
		in.nextLine();
		try {
			System.out.println(ft.athleteData(idTracker));
		} catch (AthleteNotExistException e) {
			System.out.println(e.getMessage());

		}

	}

	private static void insertActivity(Scanner in, FitnessTracker ft) {
		String idActivity = in.next().trim();
		int MET = in.nextInt();
		String name = in.nextLine().trim();

		try {
			ft.insertActivity(idActivity, MET, name);
			System.out.println("Atividade criada com sucesso.");
		} catch (InvalidMETException e) {
			System.out.println(e.getMessage());

		} catch (ActivityAlreadyExistExcpetion e) {
			System.out.println(e.getMessage());
		}
	}

	private static void addTraining(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().trim();
		String idActivity = in.next().trim();
		int time = in.nextInt();

		try {
			ft.addTraining(idTracker, idActivity, time);
			System.out.println("Treino adicionado com sucesso.");
		} catch (AthleteNotExistException e) {
			System.out.println(e.getMessage());
		} catch (ActivityNotExistException e) {
			System.out.println(e.getMessage());
		} catch (InvalidTimeException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void upgradeSteps(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().trim();
		int steps = in.nextInt();
		in.nextLine();

		try {
			ft.upgradeSteps(idTracker, steps);
			System.out.println("Atualizacao de passos com sucesso.");
		} catch (InvalidNumberStepsException e) {
			System.out.println(e.getMessage());
		} catch (AthleteNotExistException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void addGroup(Scanner in, FitnessTracker ft) {
		String idGroup = in.next().trim();
		String name = in.next().trim();
		in.nextLine();

		try {
			ft.addGroup(idGroup, name);
			System.out.println("Grupo criado com sucesso.");
		} catch (GroupAlreadyExistExecption e) {
			System.out.println(e.getMessage());
		}
	}

	private static void addAthleteAtGroup(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().trim();
		String idGroup = in.next().trim();
		in.nextLine();

		try {
			ft.addAthleteAtGroup(idTracker, idGroup);
			System.out.println("Adesao realizada com sucesso.");
		} catch (AthleteNotExistException e) {
			System.out.println(e.getMessage());
		} catch (GroupNotExistException e) {
			System.out.println(e.getMessage());
		} catch (AthleteAlreadyHasGroupException e) {
			System.out.println(e.getMessage());
		}

	}

	private static void removeAthleteAtGroup(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().trim();
		String idGroup = in.next().trim();
		in.nextLine();

		try {
			ft.removeAthleteAtGroup(idTracker, idGroup);
			System.out.println("Desistencia realizada com sucesso.");
		} catch (AthleteNotExistException e) {
			System.out.println(e.getMessage());
		} catch (GroupNotExistException e) {
			System.out.println(e.getMessage());
		} catch (AthleteDontBelongInGroupException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void groupData(Scanner in, FitnessTracker ft) {
		String idGroup = in.next().trim();
		in.nextLine();

		try {
			System.out.println(ft.groupData(idGroup));
		} catch (GroupNotExistException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void checkAthleteTraining(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().trim();
		String type = in.next().trim();
		Iterator<Training> it;

		try {
			it = ft.checkAthleteTraining(idTracker, type);
			while (it.hasNext()) {
				System.out.println(it.next());
			}
		} catch (InvalidOptionException e) {
			System.out.println(e.getMessage());
		} catch (AthleteNotExistException e) {
			System.out.println(e.getMessage());
		} catch (AthleteWithNoTrainingException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static void listWalkers(FitnessTracker ft) {
		try{
			System.out.println(ft.listWalkers());
		}
		catch(GroupsDoesntExistException e){
			System.out.println(e.getMessage());
		}
	}
	
	private static void listWarriors(FitnessTracker ft) {
		try{
			System.out.println(ft.listWarriors());
		}
		catch(GroupsDoesntExistException e){
			System.out.println(e.getMessage());
		}
	}
}
