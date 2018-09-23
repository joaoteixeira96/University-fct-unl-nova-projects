package FitnessTracker;

import java.io.Serializable;

import dataStructures.Iterator;
import exceptions.*;

/**
 * @author Pedro Xavier (47525) <p.xaviercampus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeiracampus.fct.unl.pt>
 */
public interface FitnessTracker extends Serializable{

	/**
	 * Insercao de um atleta no sistema
	 * @param idTracker id do atleta
	 * @param name nome do atleta
	 * @param heigth altura do atleta
	 * @param weigth peso do atleta
	 * @param age idade do atleta
	 * @param gender sexo do atleta
	 * @throws AthleteAlreadyExistException Atleta existente. Quando o identificador do atleta ja existe no sistema.
	 * @throws InvalidValueException Valores invalidos. Se o peso, a altura ou a idade forem nao positivos
	 * 		   ou se o carater que representa o sexo do atleta nao for valido.
	 */
	void insertAthlete(String idTracker, String name, int heigth, int weigth, int age, String gender) throws AthleteAlreadyExistException, InvalidValueException;

	/**
	 * Muda dados de um atleta
	 * @param idTracker id do atleta
	 * @param heigth altura do atleta
	 * @param weigth peso do atleta
	 * @param age idade do atleta
	 * @throws InvalidValueException Valores invalidos. Se o peso, a altura ou a idade forem nao positivos.
	 * @throws AthleteNotExistException Atleta inexistente. Quando o identificador do atleta nao existe no sistema.
	 */
	void changeAthleteData(String idTracker, int heigth, int weigth, int age) throws InvalidValueException, AthleteNotExistException;

	/**
	 * Remove atleta do sistema
	 * @param idTracker id do atleta
	 * @throws AthleteNotExistException Atleta inexistente. Quando o identificador do atleta nao existe no sistema.
	 */
	void removeAthlete(String idTracker) throws AthleteNotExistException;

	/**
	 * Devolve os dados de um atleta
	 * @param idTracker id do atleta
	 * @return dados do atleta (nome,sexo,peso,idade,calorias,passos) e grupo caso pertenca a algum grupo
	 * @throws AthleteNotExistException Atleta inexistente. Quando o identificador do atleta nao existe no sistema.
	 */
	String athleteData(String idTracker) throws AthleteNotExistException;

	/**
	 * Insere atividade no sistema
	 * @param idActivity id da atividade
	 * @param met equivalente metabolico (MET) da atividade
	 * @param name nome da atividade
	 * @throws InvalidMETException MET invalido. Quando o valor do MET for nao positivo.
	 * @throws ActivityAlreadyExistExcpetion Atividade existente. Quando o identificador da atividade ja existe no sistema.
	 */
	void insertActivity(String idActivity, int met, String name) throws InvalidMETException, ActivityAlreadyExistExcpetion;

	/**
	 * Adiciona treino ao sistema
	 * @param idTracker id do atleta
	 * @param idActivity id da atividade
	 * @param time duracao do treino
	 * @throws InvalidTimeException Tempo invalido. Quando o tempo de realizacao da atividade for nao positivo.
	 * @throws AthleteNotExistException Atleta inexistente. Quando o identificador do atleta nao existe no sistema.
	 * @throws ActivityNotExistException Atividade inexistente. Quando o identificador da atividade nao existe no sistema.
	 */
	void addTraining(String idTracker, String idActivity, int time) throws InvalidTimeException, AthleteNotExistException, ActivityNotExistException;

	/**
	 * Listagem dos treinos do atleta identificado por idTracker
	 * @param idTracker id do atleta
	 * @param type Tipo de listagem
	 * @return lista de treinos (nome da atividade, calorias)
	 * @throws AthleteNotExistException Atleta inexistente. Quando o identificador do atleta nao existe no sistema. 
	 * @throws InvalidOptionException Opcao invalida. Quando a opcao de listagem nao e valida.
	 * @throws AthleteWithNoTrainingException Atleta sem treinos. Quando o atleta nao realizou nenhum treino.
	 */
	Iterator<Training> checkAthleteTraining(String idTracker, String type) throws AthleteNotExistException, InvalidOptionException, AthleteWithNoTrainingException;

	/**
	 * Atualiza passos do atleta
	 * @param idTracker id do atleta
	 * @param steps passos dados
	 * @throws AthleteNotExistException Atleta inexistente. Quando o identificador do atleta nao existe no sistema.
	 * @throws InvalidNumberStepsException Numero de passos invalido. Quando o numero de passos e nao positivo.
	 */
	void upgradeSteps(String idTracker, int steps) throws AthleteNotExistException, InvalidNumberStepsException;

	/**
	 * Adiciona grupo no sistema
	 * @param idGroup id do grupo
	 * @param name nome de grupo
	 * @throws GroupAlreadyExistExecption Grupo ja existente. Quando o identificador do grupo ja existe no sistema.
	 */
	void addGroup(String idGroup, String name) throws GroupAlreadyExistExecption;

	/**
	 * Adiciona atleta a um grupo
	 * @param idTracker id do atleta
	 * @param idGroup id do grupo
	 * @throws AthleteNotExistException Atleta inexistente. Quando o identificador do atleta nao existe no sistema.
	 * @throws AthleteAlreadyHasGroupException Atleta ja tem grupo. Quando o atleta ja pertence a um grupo.
	 * @throws GroupNotExistException Grupo inexistente. Quando o identificador do grupo nao existe no sistema.
	 */
	void addAthleteAtGroup(String idTracker, String idGroup) throws AthleteNotExistException, AthleteAlreadyHasGroupException, GroupNotExistException;


	/**
	 * Remove atleta do seu grupo
	 * @param idTracker id do atleta
	 * @param idGroup id do grupo
	 * @throws AthleteNotExistException Atleta inexistente. Quando o identificador do atleta nao existe no sistema.
	 * @throws GroupNotExistException Grupo inexistente. Quando o identificador do grupo nao existe no sistema.
	 * @throws AthleteDontBelongInGroupException Atleta nao pertence ao grupo. Quando atleta nao pertence ao grupo referido 
	 * 		  (pertence a outro grupo ou nao pertence a nenhum).
	 */
	void removeAthleteAtGroup(String idTracker, String idGroup) throws AthleteNotExistException, GroupNotExistException, AthleteDontBelongInGroupException;

	/**
	 * Consulta dos resultados do grupo identificado pelo idGroup
	 * @param idGroup id do grupo
	 * @return dados do grupo (nome, calorias e passos)
	 * @throws GroupNotExistException Grupo inexistente. Quando o identificador do grupo nao existe no sistema.
	 */
	String groupData(String idGroup) throws GroupNotExistException;

	/**
	 * Listagem da ordenacao dos grupos pelo numero de passos, do maior para o mais pequeno
	 * @return dados dos grupos ordenados pelo numero de passos
	 * @throws GroupsDoesntExistException Nao existem grupos. Quando o sistema nao tem grupos.
	 */
	Group listWalkers() throws GroupsDoesntExistException;

	/**
	 * Listagem da ordenacao dos grupos pelo numero de calorias perdidas, do maior para o mais pequeno
	 * @return dados dos grupos ordenados pelo numero de calorias perdidas
	 * @throws GroupsDoesntExistException Nao existem grupos. Quando o sistema nao tem grupos.
	 */
	Group listWarriors() throws GroupsDoesntExistException;

}
