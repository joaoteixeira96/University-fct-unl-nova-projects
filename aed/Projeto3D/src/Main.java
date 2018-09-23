
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import FitnessTracker.*;
import FitnessTracker.exceptions.*;
import dataStructures.Entry;
import dataStructures.Iterator;
import dataStructures.List;

/**
 * @author Pedro Xavier (47525) <p.xaviercampus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeiracampus.fct.unl.pt>
 */
public class Main {
	private static final String GRUPO_NAO_TEM_ADESOES = "Grupo nao tem adesoes.";
	private static final String ATIVIDADE_CRIADA_COM_SUCESSO = "Atividade criada com sucesso.";
	private static final String ATIVIDADE_EXISTENTE = "Atividade existente.";
	private static final String TEMPO_INVALIDO = "Tempo invalido.";
	private static final String NAO_EXISTEM_GRUPOS = "Nao existem grupos.";
	private static final String ATLETA_SEM_TREINOS = "Atleta sem treinos.";
	private static final String OPCAO_INVALIDA = "Opcao invalida.";
	private static final String ATLETA_NAO_PERTENCE_AO_GRUPO = "Atleta nao pertence ao grupo.";
	private static final String DESISTENCIA_REALIZADA_COM_SUCESSO = "Desistencia realizada com sucesso.";
	private static final String ADESAO_REALIZADA_COM_SUCESSO = "Adesao realizada com sucesso.";
	private static final String ATLETA_JA_TEM_GRUPO = "Atleta ja tem grupo.";
	private static final String GRUPO_INEXISTENTE = "Grupo inexistente.";
	private static final String GRUPO_EXISTENTE = "Grupo existente.";
	private static final String GRUPO_CRIADO_COM_SUCESSO = "Grupo criado com sucesso.";
	private static final String ATUALIZACAO_DE_PASSOS_COM_SUCESSO = "Atualizacao de passos com sucesso.";
	private static final String NUMERO_DE_PASSOS_INVALIDO = "Numero de passos invalido.";
	private static final String TREINO_ADICIONADO_COM_SUCESSO = "Treino adicionado com sucesso.";
	private static final String ATIVIDADE_INEXISTENTE = "Atividade inexistente.";
	private static final String MET_INVALIDO = "MET invalido.";
	private static final String ATLETA_INEXISTENTE = "Atleta inexistente.";
	private static final String ATLETA_REMOVIDO_COM_SUCESSO = "Atleta removido com sucesso.";
	private static final String ATLETA_ATUALIZADO_COM_SUCESSO = "Atleta atualizado com sucesso.";
	private static final String INSERCAO_DE_ATLETA_COM_SUCESSO = "Insercao de atleta com sucesso.";
	private static final String ATLETA_EXISTENTE = "Atleta existente.";
	private static final String VALORES_INVALIDOS = "Valores invalidos.";
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
	private static final String LISTAR_GRUPO = "LG";
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
			case LISTAR_GRUPO:
				listGroup(in, ft);
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
		// in.close();

	}

	private static void save(FitnessTracker ft) throws IOException {
		try {
			ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(DATA_FILE));
			file.writeObject(ft);
			file.flush();
			file.close();
		} catch (IOException e) {
			System.out.println();
		}
	}

	private static FitnessTracker load() throws FileNotFoundException, IOException, ClassNotFoundException {
		FitnessTracker ft = null;
		try {
			ObjectInputStream file = new ObjectInputStream(new FileInputStream(DATA_FILE));
			ft = (FitnessTracker) file.readObject();
			file.close();
		}

		catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			ft = new FitnessTrackerClass();
		}
		return ft;
	}

	private static String getCommand(Scanner in) {
		String input = in.next().trim();
		return input;
	}

	private static void insertAthlete(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().toUpperCase().trim();
		int weigth = in.nextInt();
		int heigth = in.nextInt();
		int age = in.nextInt();
		String gender = in.next().toUpperCase().trim();
		String name = in.nextLine().trim();

		try {
			ft.insertAthlete(idTracker, name, heigth, weigth, age, gender);
			System.out.println(INSERCAO_DE_ATLETA_COM_SUCESSO);
		} catch (InvalidValueException e) {
			System.out.println(VALORES_INVALIDOS);
		} catch (AthleteAlreadyExistException e) {
			System.out.println(ATLETA_EXISTENTE);
		}

	}

	private static void changeAthleteData(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().toUpperCase().trim();
		int weigth = in.nextInt();
		int heigth = in.nextInt();
		int age = in.nextInt();
		try {
			ft.changeAthleteData(idTracker, heigth, weigth, age);
			System.out.println(ATLETA_ATUALIZADO_COM_SUCESSO);
		} catch (InvalidValueException e) {
			System.out.println(VALORES_INVALIDOS);
		} catch (AthleteNotExistException e) {
			System.out.println(ATLETA_INEXISTENTE);
		}

	}

	private static void removeAthlete(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().toUpperCase().trim();
		in.nextLine();
		try {
			ft.removeAthlete(idTracker);
			System.out.println(ATLETA_REMOVIDO_COM_SUCESSO);
		} catch (AthleteNotExistException e) {
			System.out.println(ATLETA_INEXISTENTE);
		}

	}

	private static void athleteData(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().toUpperCase().trim();
		in.nextLine();
		try {
			AthleteGet atl = ft.athleteData(idTracker);
			if(atl.groupOfAthlete() != null)
				System.out.println(atl + " (" + atl.groupOfAthlete().getName() + ")");
			else
				System.out.println(atl);
			
		} catch (AthleteNotExistException e) {
			System.out.println(ATLETA_INEXISTENTE);

		}

	}

	private static void insertActivity(Scanner in, FitnessTracker ft) {
		String idActivity = in.next().trim();
		int MET = in.nextInt();
		String name = in.nextLine().trim();

		try {
			ft.insertActivity(idActivity, MET, name);
			System.out.println(ATIVIDADE_CRIADA_COM_SUCESSO);
		} catch (InvalidMETException e) {
			System.out.println(MET_INVALIDO);

		} catch (ActivityAlreadyExistExcpetion e) {
			System.out.println(ATIVIDADE_EXISTENTE);
		}
	}

	private static void addTraining(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().toUpperCase().trim();
		String idActivity = in.next().trim();
		int time = in.nextInt();

		try {
			ft.addTraining(idTracker, idActivity, time);
			System.out.println(TREINO_ADICIONADO_COM_SUCESSO);
		} catch (AthleteNotExistException e) {
			System.out.println(ATLETA_INEXISTENTE);
		} catch (ActivityNotExistException e) {
			System.out.println(ATIVIDADE_INEXISTENTE);
		} catch (InvalidTimeException e) {
			System.out.println(TEMPO_INVALIDO);
		}
	}

	private static void upgradeSteps(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().toUpperCase().trim();
		int steps = in.nextInt();
		in.nextLine();

		try {
			ft.upgradeSteps(idTracker, steps);
			System.out.println(ATUALIZACAO_DE_PASSOS_COM_SUCESSO);
		} catch (InvalidNumberStepsException e) {
			System.out.println(NUMERO_DE_PASSOS_INVALIDO);
		} catch (AthleteNotExistException e) {
			System.out.println(ATLETA_INEXISTENTE);
		}
	}

	private static void addGroup(Scanner in, FitnessTracker ft) {
		String idGroup = in.next().trim();
		String name = in.next().trim();
		in.nextLine();

		try {
			ft.addGroup(idGroup, name);
			System.out.println(GRUPO_CRIADO_COM_SUCESSO);
		} catch (GroupAlreadyExistExecption e) {
			System.out.println(GRUPO_EXISTENTE);
		}
	}

	private static void addAthleteAtGroup(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().toUpperCase().trim();
		String idGroup = in.next().trim();
		in.nextLine();

		try {
			ft.addAthleteAtGroup(idTracker, idGroup);
			System.out.println(ADESAO_REALIZADA_COM_SUCESSO);
		} catch (AthleteNotExistException e) {
			System.out.println(ATLETA_INEXISTENTE);
		} catch (GroupNotExistException e) {
			System.out.println(GRUPO_INEXISTENTE);
		} catch (AthleteAlreadyHasGroupException e) {
			System.out.println(ATLETA_JA_TEM_GRUPO);
		}

	}

	private static void removeAthleteAtGroup(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().toUpperCase().trim();
		String idGroup = in.next().trim();
		in.nextLine();

		try {
			ft.removeAthleteAtGroup(idTracker, idGroup);
			System.out.println(DESISTENCIA_REALIZADA_COM_SUCESSO);
		} catch (AthleteNotExistException e) {
			System.out.println(ATLETA_INEXISTENTE);
		} catch (GroupNotExistException e) {
			System.out.println(GRUPO_INEXISTENTE);
		} catch (AthleteDontBelongInGroupException e) {
			System.out.println(ATLETA_NAO_PERTENCE_AO_GRUPO);
		}
	}

	private static void groupData(Scanner in, FitnessTracker ft) {
		String idGroup = in.next().trim();
		in.nextLine();

		try {
			System.out.println(ft.groupData(idGroup));
		} catch (GroupNotExistException e) {
			System.out.println(GRUPO_INEXISTENTE);
		}
	}

	private static void listGroup(Scanner in, FitnessTracker ft) {
		String idGroup = in.next().trim();
		in.nextLine();
		Iterator<Entry<String, AthleteGet>> it;
		try {
			it = ft.ListGroupAthtele(idGroup);
			while (it.hasNext()) {
				System.out.println(it.next().getValue());
			}
		} catch (GroupNotExistException e) {
			System.out.println(GRUPO_INEXISTENTE);
		} catch (GroupDoesntHaveAthletesException e) {
			System.out.println(GRUPO_NAO_TEM_ADESOES);
		} 

	}

	private static void checkAthleteTraining(Scanner in, FitnessTracker ft) {
		String idTracker = in.next().toUpperCase().trim();
		String type = in.next().trim();
		Iterator<Training> it;
		Iterator<Entry<Integer, List<Training>>> lit;
		try {
			if (!(type.equalsIgnoreCase("T") || type.equalsIgnoreCase("C")))
				throw new InvalidOptionException();
			if(type.equalsIgnoreCase("T")){
				it = ft.listTrainingByChronologicalOrder(idTracker);
				while (it.hasNext()) {
					System.out.println(it.next());
				}
			}
			else{
				lit = ft.listTrainingByCalories(idTracker);
				while (lit.hasNext()) {
					Iterator<Training> gt = lit.next().getValue().iterator();
					while(gt.hasNext()){
						System.out.println(gt.next());
					}
				}
			}
		} catch (InvalidOptionException e) {
			System.out.println(OPCAO_INVALIDA);
		} catch (AthleteNotExistException e) {
			System.out.println(ATLETA_INEXISTENTE);
		} catch (AthleteWithNoTrainingException e) {
			System.out.println(ATLETA_SEM_TREINOS);
		}
	}

	private static void listWalkers(FitnessTracker ft) {
		try {
			Iterator<Entry<Integer, List<GroupGet>>> it;
			it = ft.listWalkers();
			while (it.hasNext()) {
				Iterator<GroupGet> gt = it.next().getValue().iterator();
				while (gt.hasNext()){
					System.out.println(gt.next());
				}
			}
		} catch (GroupsDoesntExistException e) {
			System.out.println(NAO_EXISTEM_GRUPOS);
		}
	}

	private static void listWarriors(FitnessTracker ft) {
		try {
			Iterator<Entry<Integer, List<GroupGet>>> it;
			it = ft.listWarriors();
			while (it.hasNext()) {
				Iterator<GroupGet> gt = it.next().getValue().iterator();
				while (gt.hasNext())
					System.out.println(gt.next());
			}
		} catch (GroupsDoesntExistException e) {
			System.out.println(NAO_EXISTEM_GRUPOS);
		}
	}
}
