drop table pessoas cascade constraints;
--CRIAR TABELA PESSOAS
create table pessoas (
nif number(9),
nome varchar2(30) not null,
email varchar2(60) not null,
sexo char(1) not null CHECK (sexo IN ( 'F' , 'M' )),  -- sexo tem de ser feminino(F) ou masculino(M) 
telefone number (15),
primary key(nif),
unique (email), -- tem que haver um email diferente por pessoa
check (nif between 100000000 and 999999999) -- Garantir que o nif se encontra entre estes numeros
);

drop table funcionarios cascade constraints;
--CRIAR TABELA FUNCIONARIOS
create table funcionarios (
nifFun number(9),
numFun number(5) not null,
primary key (nifFun),
foreign key(nifFun) references pessoas(nif),
unique (numFun) -- tem que haver um numero de funcionario(numFun) diferente para cada funcionario 
);

drop table clientes cascade constraints;
--CRIAR TABELA CLIENTES
create table clientes (
nifCli number(9),
numCli number(5) not null,
nacionalidade varchar2(30),
primary key (nifCli),
foreign key(nifCli) references pessoas(nif),
unique (numCli)  -- tem que haver um numero de cliente(numCli) diferente para cada cliente 
);
  
drop table administradores cascade constraints;
--CRIAR TABELA ADMINISTRADORES
create table administradores (
nifAd number(9),
cargo varchar2(2)not null,
numAd number(5),
primary key (nifAd),
foreign key(nifAd) references pessoas(nif),
check (cargo in ('G', 'SG', 'P')),-- G -> Gerente | SP -> subGerente | P -> patrao , so existe estes 3 cargos para administradores
unique(numAd)
);

drop table sectores cascade constraints;
--CRIAR TABELA SECTORES
create table sectores (
codigoSector varchar2(2),
descricao varchar2(30),
primary key (codigoSector)
);

drop table trabalhaEm cascade constraints;
--CRIAR TABELA TRABALHA EM
create table trabalhaEm (
nifFun number(9),
codigoSector varchar2(2),
nifAd number(9),
primary key (nifFun,codigoSector),
foreign Key (nifFun) references funcionarios(nifFun),
foreign key (codigoSector) references sectores(codigoSector),
foreign key (nifAd) references administradores(nifAd)
);

drop table tipoQuartos cascade constraints;
--CRIAR TABELA TIPO QUARTOS
create table tipoQuartos (
codigoQ varchar(2),
capacidade number(1) not null,
precoBase number(3) not null,
primary key (codigoQ)
);

drop table quartos cascade constraints;
--CRIAR TABELA QUARTOS
create table quartos (
nQuarto number(2),
codigoQ varchar2(2),
piso number(2) not null,
primary key (nQuarto),
foreign key (codigoQ) references tipoQuartos(codigoQ)
);

drop table manutencao cascade constraints;
--CRIAR TABELA MANUTENCAO
create table manutencao (
nManutencao number(5),
dataManutencao DATE,
nQuarto number(2),
nifFunMan number(9),
primary key (dataManutencao,nQuarto),
foreign key (nQuarto) references quartos(nQuarto),
foreign key (nifFunMan) references funcionarios(nifFun),
unique(nManutencao)
);

drop table facilidades cascade constraints;
--CRIAR TABELA FACILIDADES
create table facilidades (
codigoFacilid varchar2(3),
descrFacilid varchar2(30) not null,
primary key (codigoFacilid)
);

drop table temFacilidades cascade constraints;
--CRIAR TABELA TEM FACILIDADES
create table temFacilidades (
nQuarto number(2),
codigoFacilid varchar2(3),
primary key (nQuarto,codigoFacilid),
foreign key (nQuarto) references quartos(nQuarto),
foreign key (codigoFacilid) references facilidades(codigoFacilid)
);

drop table reservas cascade constraints;
--CRIAR TABELA RESERVAS
create table reservas (
nReserva number(10),
nifCli number(9),
nQuarto number(2) not null,
preco number(10) not null,
dataEntrada DATE not null,
dataSaida DATE not null,
capacidadeR number(1) not null,
primary key (nReserva),
foreign key (nifCli) references clientes(nifCli),
foreign key (nQuarto) references quartos(nQuarto),
check (preco > 0),
check (dataEntrada < dataSaida)  -- o preco para a reserva de um quarto(preco) terá que ser positivo nao nulo e a data de entrada inferior a de saida 
);

drop table checkOut cascade constraints;
--CRIAR TABELA CHECKOUT
create table checkOut(
dataCheckOut DATE,
nReserva number(10),
classificacao number(1)not null,
nifFun number(9),
nCheckOut number(5),
primary key (dataCheckOut, nReserva),
foreign key (nifFun) references funcionarios(nifFun),
foreign key (nReserva) references reservas(nReserva),
check (classificacao in (1,2,3,4,5)), -- no checkOut o cliente deixa sempre uma classificacao pelo servico/estadia do hotel tendo essa a variar de 1 a 5 
unique(nCheckOut)
);


--SEQUENCIAS

--CRIAR SEQUENCIA PARA NUMERO CLIENTE
drop sequence seq_clientes;
create sequence seq_clientes increment by 1 start with 1;

--CRIAR SEQUENCIA PARA NUMERO ADMINISTRADORES
drop sequence seq_administradores;
create sequence seq_administradores increment by 1 start with 1;

--CRIAR SEQUENCIA PARA NUMERO FUNCIONARIO
drop sequence seq_funcionarios;
create sequence seq_funcionarios increment by 1 start with 1;

--CRIAR SEQUENCIA PARA NUMERO RESERVA
drop sequence seq_reservas;
create sequence seq_reservas increment by 1 start with 1;

--CRIAR SEQUENCIA PARA NUMERO CHECKOUT
drop sequence seq_checkout;
create sequence seq_checkout increment by 1 start with 1;

--CRIAR SEQUENCIA PARA NUMERO MANUTENCAO
drop sequence seq_manutencao;
create sequence seq_manutencao increment by 1 start with 1;

--VIEWS

--CRIAR VIEW CLIENTES
drop view v_clientes cascade constraints;
create or replace view v_clientes as
select numCli, nome, nif, nacionalidade, email, sexo, telefone
from clientes inner join pessoas on(pessoas.nif = clientes.nifCli)
order by numCli;

--CRIAR VIEW ADMINISTRADORES
drop view v_administradores cascade constraints;
create or replace view v_administradores as
select numAd, nome, nif, cargo, email, sexo, telefone
from administradores inner join pessoas on(pessoas.nif = administradores.nifAd)
order by numAd;

--CRIAR VIEW FUNCIONARIOS
drop view v_funcionarios cascade constraints;
create or replace view v_funcionarios as
select numFun, nif, nome, email, sexo, telefone
from funcionarios inner join pessoas on(pessoas.nif = funcionarios.nifFun)
order by numFun;

--CRIAR VIEW RESERVAS
drop view v_reservas cascade constraints;
create or replace view v_reservas as
select nReserva, capacidadeR , nQuarto, preco, dataEntrada, dataSaida, nifCli, nome, email, sexo, telefone, nacionalidade
from reservas inner join clientes using(nifCli) inner join pessoas on(pessoas.nif = nifCli)
order by nReserva desc;

--CRIAR VIEW CHECKOUT
drop view v_checkout cascade constraints;
create or replace view v_checkout as
select nCheckOut, nReserva, classificacao, nifCli, dataCheckout, nifFun
from checkOut natural join reservas
order by nCheckOut desc;

--CRIARI VIEW FACILIDADES DOS QUARTOS
drop view v_facilidadesQuarto cascade constraints;
create or replace view v_facilidadesQuarto as
select ROWID pk, nQuarto, codigoFacilid
from temFacilidades;

--CRIAR VIEW DE QUARTOS
drop view v_quartos cascade constraints;
create or replace view v_quartos as
select nQuarto, nQuarto as numeroQuarto, codigoQ ,piso from quartos;

--CRIAR VIEW MANUTENCAO
drop view v_manutencao cascade constraints;
create or replace view v_manutencao as
select nManutencao, dataManutencao, nQuarto, nifFunMan, nome
from manutencao inner join funcionarios on(nifFunMan = nifFun) inner join pessoas on(nifFunMan = nif)
order by dataManutencao desc;

--CRIAR VIEW PARA TRABALHA EM
drop view v_trabalhaEm cascade constraints;
create or replace view v_trabalhaEm as
select ROWID pk, nifFun, codigoSector, nifAd
from trabalhaEm;

--CRIAR VIEW PARA TIPO DE QUARTOS
drop view  v_tipoQuartos;
create or replace view v_tipoQuartos as
SELECT * FROM tipoQuartos ;

--TRIGGERS

--INSERIR ADMINISTRADORES
create or replace trigger insert_administradores
instead of insert on v_administradores
referencing new as NEW
for each row
    declare numero number;
begin
    select count(*) into numero
    from pessoas
    where nif=:NEW.nif;

    if numero > 0 then
        insert into administradores
        values (:NEW.nif, :NEW.cargo, seq_administradores.nextval);
    end if;

    if numero < 1 then
        insert into pessoas
        values (:NEW.nif, :NEW.nome, :NEW.email, :NEW.sexo, :NEW.telefone);

        insert into administradores
        values (:NEW.nif, :NEW.cargo, seq_administradores.nextval);
        end if;
end;
/

--UPDATE ADMINISTRADORES
create or replace trigger update_administradores
instead of update on v_administradores
referencing new as new old as old
for each row
begin
    update pessoas
    set nome=:new.nome,
    email=:new.email,
    sexo=:new.sexo,
    telefone=:new.telefone
    where nif=:new.nif;
    
    update administradores
    set cargo = :new.cargo
    where nifAd = :new.nif; 
end;
/

--INSERIR FUNCIONARIOS
create or replace trigger insert_funcionarios
instead of insert on v_funcionarios
referencing new as NEW
for each row
    declare numero number;
begin
    select count(*) into numero
    from pessoas
    where nif=:NEW.nif;

    if numero > 0 then
        insert into funcionarios
        values (:NEW.nif, seq_funcionarios.nextval);
    end if;

    if numero < 1 then
        insert into pessoas
        values (:NEW.nif, :NEW.nome, :NEW.email, :NEW.sexo, :NEW.telefone);

        insert into funcionarios
        values (:NEW.nif, seq_funcionarios.nextval);
        end if;
end;
/

--UPDATE FUNCIONARIOS
create or replace trigger update_funcionarios
instead of update on v_funcionarios
referencing new as new old as old
for each row
begin
    update pessoas
    set nome=:new.nome,
    email=:new.email,
    sexo=:new.sexo,
    telefone=:new.telefone
    where nif=:new.nif;
end;
/

--INSERIR NA TABELA TRABALHA EM
create or replace trigger insert_trabalhaem
instead of insert on v_trabalhaem
referencing new as NEW
for each row
begin
    insert into trabalhaem (nifFun ,codigoSector , nifAd)
    values (:NEW.nifFun,:NEW.codigoSector,:NEW.nifAd);
end;
/

--DELETE NA TABELA TRABALHA EM
create or replace TRIGGER delete_trabalhaEm
instead of delete on v_trabalhaEm
referencing old as OLD
for each row
begin
    delete from trabalhaem where nifFun = :OLD.nifFun;
end;
/

--UPDATE CLIENTES
create or replace trigger update_clientes
instead of update on v_clientes
referencing new as NEW old as OLD
for each row
begin
    update pessoas
    set nome = :NEW.nome,
    email = :NEW.email, 
    telefone = :NEW.telefone,
    sexo = :NEW.sexo
    where nif = :NEW.nif;
    
    update clientes
    set nacionalidade = :NEW.nacionalidade
    where nifCli = :NEW.nif;
end;
/

--INSERIR RESERVA
create or replace trigger insert_reserva
instead of insert on v_reservas
referencing new as NEW
for each row
    declare quarto number; cliente number; precoR number;
begin
        select count(*) into cliente --ver se o cliente que esta a reservar ja existe
        from pessoas where nif=:NEW.nifCli;

        if cliente < 1 then --se nao existir insersir
            insert into pessoas values (:NEW.nifCli, :NEW.nome, :NEW.email, :NEW.sexo, :NEW.telefone);
            insert into clientes values (:NEW.nifCli, seq_clientes.nextval, :NEW.nacionalidade);
        end if;
                    
        select min(nQuarto) into quarto from ((
            select nQuarto from quartos inner join tipoQuartos using(codigoQ) where :NEW.capacidadeR = capacidade)
            minus
            (select nQuarto from reservas
            where :NEW.dataEntrada < dataSaida and :NEW.dataSaida > dataEntrada));
            
            select precoBase * (:NEW.datasaida-:NEW.dataentrada) into precoR
            from tipoquartos
            where :NEW.capacidadeR = capacidade;
            
         insert into reservas values(seq_reservas.nextval, :NEW.nifcli, quarto, precoR, :NEW.dataEntrada, :NEW.dataSaida, :NEW.capacidadeR);
         
         if quarto < 1 then
          raise_application_error(-20002,'Lamentamos mas nao temos disponibilidade para as suas datas');
         end if;
end;
/

--INSERIR CHECKOUT
create or replace trigger insert_checkout
instead of insert on v_checkout
referencing new as NEW
for each row
    declare quarto number;
begin
    insert into checkOut
    values (:NEW.dataCheckout, :NEW.nReserva, :NEW.classificacao, :NEW.nifFun, seq_checkout.nextval);
end;
/

--INSERIR MANUTENCAO
create or replace trigger insert_manutencao
instead of insert on v_manutencao
referencing new as NEW
for each row
begin
        insert into manutencao values(seq_manutencao.nextval, :NEW.dataManutencao, :NEW.nQuarto, :NEW.nifFunMan);
end;
/

--INSERIR FACILIDADES EM QUARTO 
create or replace trigger insert_temfacilidadeQuarto
instead of insert on v_facilidadesQuarto
referencing new as NEW
for each row
begin
    insert into temfacilidades (nQuarto,codigoFacilid)
    values (:NEW.nQuarto,:NEW.codigoFacilid);
end;
/

--DELETE FACILIDADES DO QUARTO
create or replace trigger delete_temfacilidadeQuarto
instead of delete on v_facilidadesQuarto
referencing old as OLD
for each row
begin
    delete from temfacilidades where ROWID = :OLD.pk;
end;
/

--ALTERAR PRECO BASE DO TIPO DE QUARTO
create or replace trigger update_preco
instead of update on v_tipoQuartos
referencing new as NEW old as OLD
for each row
begin
    update tipoQuartos
    set precoBase = :NEW.precoBase
    where capacidade = :NEW.capacidade;
end;
/

grant insert on v_reservas to BD47406;
grant select on v_tipoQuartos to BD47406;

grant select on v_funcionarios to BD47981;
grant insert on v_checkout to BD47981;
grant select on v_clientes to BD47981;
grant select on v_facilidadesquarto to BD47981;
grant insert, select on v_manutencao to BD47981;
grant select on v_quartos to BD47981;
grant select, insert on v_reservas to BD47981;
grant select on v_trabalhaEm to BD47981;

--Inserir na view administradores/
insert into v_administradores values (1, 'Ana Sousa', 123456789, 'G', 'ana.sousa@gmail.com', 'F', 922209090);
insert into v_administradores values (2, 'Joao Teixeira', 134567899, 'SG', 'joaoteixeira@gmail.com', 'M', 967845323);
insert into v_administradores values (3, 'Mafalda Camilo', 145678901, 'P', 'maf.camilo@gmail.com', 'F', 956827468);
insert into v_administradores values (4, 'Miguel Pereira', 156789012, 'G', 'miguel.pereira@gmail.com', 'M', 937563837);
insert into v_administradores values (5, 'Claudio Jesus', 167890123, 'SG', 'claudio.jesus@gmail.com', 'M', 947563846);
insert into v_administradores values (6, 'Barbara Cruz', 178901234, 'P', 'barbara.cruz@gmail.com', 'F', 9143645736);

--Inserir na view funcionarios/
insert into v_funcionarios values (1, 126385724, 'Albertina Silva', 'albertina@gmail.com', 'F', 978365849 );
insert into v_funcionarios values (2, 137597504, 'Afonso Ferreira', 'afonso.ferreira@gmail.com', 'M', 983973745);
insert into v_funcionarios values (3, 173928478, 'Joana Santos', 'joana.santos@gmail.com', 'F', 903756383);
insert into v_funcionarios values (4, 147976896, 'Marta Oliveira', 'marta.oliveira@live.com.pt', 'F', 937465867);
insert into v_funcionarios values (5, 159347987, 'Beatriz Costa', 'beatriz.costa@live.com.pt',  'F', 937566635);
insert into v_funcionarios values (6, 175985402, 'David Lopes', 'david.lopes@hotmail.com', 'M', 912746574);
insert into v_funcionarios values (7, 186892097, 'Ania Marques', 'ania.marques@gmail.com', 'F', 974658375);
insert into v_funcionarios values (8, 174957593, 'Filipe Alves', 'filipe.alves@hotmail.com', 'M', 947563648);
insert into v_funcionarios values (9, 174927495, 'Paula Almeida', 'paula.almeida@live.com.pt', 'F', 937563756);
insert into v_funcionarios values (10, 144957326, 'Rodrigo Pinto', 'rodrigo.pinto@hotmail.com', 'M', 984657387);
insert into v_funcionarios values (11, 169573028, 'Diogo Moreira', 'diogo.moreira@gmail.com', 'M', 937563638);
insert into v_funcionarios values (12, 174920475, 'Pedro Mendes', 'pedro.mendes@hotmail.com', 'M', 938463826);
insert into v_funcionarios values (13, 138507264, 'Margarida Reis', 'margarida.reis@live.com.pt', 'F', 937657364);
commit;
--Inserir Sector/
insert into Sectores values ('L','Limpeza');
insert into Sectores values ('R1','Recepcao');
insert into Sectores values ('M','Marketing');
insert into Sectores values ('R2','Recursos_Humanos');
insert into Sectores values ('R3','Restauracao');
insert into Sectores values ('F','Financas');
insert into Sectores values ('R4','Relacoes_publicas');
insert into Sectores values ('S','Seguranca');

--Inserir TrabalhaEm/
--delete from trabalhaEm
insert into trabalhaEm values (126385724, 'L', 123456789);
insert into trabalhaEm values (137597504, 'R1', 134567899);
insert into trabalhaEm values (137597504, 'M', 145678901);
insert into trabalhaEm values (173928478, 'R2', 156789012);
insert into trabalhaEm values (147976896, 'R3', 167890123);
insert into trabalhaEm values (159347987, 'F', 178901234);
insert into trabalhaEm values (175985402, 'R4', 178901234);
insert into trabalhaEm values (186892097, 'S', 145678901);
insert into trabalhaEm values (174957593, 'L', 134567899);
insert into trabalhaEm values (174927495, 'R1', 167890123);
insert into trabalhaEm values (144957326, 'M', 145678901);
insert into trabalhaEm values (169573028, 'R2', 123456789);
insert into trabalhaEm values (174920475, 'R3', 134567899);
insert into trabalhaEm values (138507264, 'F', 156789012);

--Inserir tipoQuartos/
insert into tipoQuartos values ('T0',2, 100);
insert into tipoQuartos values ('T1',4, 200);
insert into tipoQuartos values ('T2',6, 300);
insert into tipoQuartos values ('T3',8, 400);

--Inserir Facilidades/
insert into facilidades values ('F','Fumador');
insert into facilidades values ('M','Minibar');
insert into facilidades values ('H','Hidromassagem');
insert into facilidades values ('C','Cofre');
insert into facilidades values ('E','Eletrodomesticos');
insert into facilidades values ('VM','Vista mar');

--Inserir quartos/
insert into quartos values (1,'T0',2);
insert into quartos values (2,'T1',2);
insert into quartos values (3,'T2',2);
insert into quartos values (4,'T1',2);
insert into quartos values (5,'T1',2);
insert into quartos values (6,'T0',3);
insert into quartos values (7,'T1',3);
insert into quartos values (8,'T3',3);
insert into quartos values (9,'T2',3);
insert into quartos values (10,'T2',3);
insert into quartos values (11,'T0',3);
insert into quartos values (12,'T2',3);
insert into quartos values (13,'T1',3);
insert into quartos values (14,'T1',3);
insert into quartos values (15,'T1',4);
insert into quartos values (16,'T3',4);
insert into quartos values (17,'T3',4);
insert into quartos values (18,'T1',4);
insert into quartos values (19,'T1',4);
insert into quartos values (20,'T0',4);

--Inserir temFacilidades/
insert into temFacilidades values(1,'F');
insert into temFacilidades values(1, 'M');
insert into temFacilidades values(2, 'VM');
insert into temFacilidades values(3, 'H');
insert into temFacilidades values(3, 'C');
insert into temFacilidades values(3, 'E');
insert into temFacilidades values(4, 'VM');
insert into temFacilidades values(4, 'F');
insert into temFacilidades values(5, 'C');
insert into temFacilidades values(6, 'H');
insert into temFacilidades values(7, 'VM');
insert into temFacilidades values(7, 'F');
insert into temFacilidades values(8, 'H');
insert into temFacilidades values(9, 'M');
insert into temFacilidades values(10, 'M');
insert into temFacilidades values(11, 'F');
insert into temFacilidades values(12, 'VM');
insert into temFacilidades values(13, 'M');
insert into temFacilidades values(13, 'H');
insert into temFacilidades values(14, 'F');
insert into temFacilidades values(15, 'C');
insert into temFacilidades values(15, 'E');
insert into temFacilidades values(16, 'E');
insert into temFacilidades values(17, 'VM');
insert into temFacilidades values(18, 'F');
insert into temFacilidades values(19, 'M');
insert into temFacilidades values(20, 'VM');

--Inserir Manutencao/
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-01', 'YYYY-MM-DD'), 1, 126385724);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-02', 'YYYY-MM-DD'), 2, 137597504);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-03', 'YYYY-MM-DD'), 6, 126385724);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-04', 'YYYY-MM-DD'), 4, 137597504);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-05', 'YYYY-MM-DD'), 3, 126385724);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-06', 'YYYY-MM-DD'), 1, 126385724);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-07', 'YYYY-MM-DD'), 5, 137597504);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-08', 'YYYY-MM-DD'), 6, 126385724);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-09', 'YYYY-MM-DD'), 8, 137597504);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-10', 'YYYY-MM-DD'), 7, 126385724);
insert into manutencao values (SEQ_MANUTENCAO.NEXTVAL, to_date('2017-06-11', 'YYYY-MM-DD'), 9, 126385724);

--Inserir Reservas/
insert into v_reservas values (1, 2, 6, 100, to_date('2017-06-01', 'YYYY-MM-DD'), to_date('2017-06-02','YYYY-MM-DD') , 122222229, 'Catarina Silva', 'catarina@gmail.com', 'F', 967854354, 'Portugal'); --
insert into v_reservas values (2, 4, 2, 600, to_date('2017-06-01', 'YYYY-MM-DD'), to_date('2017-06-04', 'YYYY-MM-DD'), 125467359, 'Alejandre Gusto', 'alej.gusto@gmail.com', 'M', 34960045275, 'Espanha'); --
insert into v_reservas values (3, 2, 1, 300, to_date('2017-06-03', 'YYYY-MM-DD'), to_date('2017-06-06', 'YYYY-MM-DD'), 164928576, 'Adeline Adri', 'adeline.adri@gmail.com' , 'F', 33987829487, 'Franca'); --
insert into v_reservas values (4, 4, 2, 200, to_date('2017-06-01', 'YYYY-MM-DD'), to_date('2017-06-02', 'YYYY-MM-DD'), 123456788,'Alessia Vicenza', 'alessiavi@gmail.com' , 'F', 39973612546, 'Italia'); --
insert into v_reservas values (5, 6, 3, 900, to_date('2017-06-01', 'YYYY-MM-DD'), to_date('2017-06-04', 'YYYY-MM-DD'), 178539485, 'Mafalda Sousa', 'mafalda@live.com.pt' , 'F', 913675364, 'Portugal'); --
insert into v_reservas values (6, 6, 9, 300, to_date('2017-06-02', 'YYYY-MM-DD'), to_date('2017-06-03', 'YYYY-MM-DD'), 134343439, 'Valentina Gonzalo', 'valentinagonzalo@gmail.com' , 'F', 34984726485, 'Espanha'); --caso com o anterior
insert into v_reservas values (7, 4, 4, 400, to_date('2017-06-02', 'YYYY-MM-DD'), to_date('2017-06-04', 'YYYY-MM-DD'), 146857897, 'Andre Jorge', 'andre@gmail.com', 'M', 937562947, 'Portugal'); --caso com o 2º
insert into v_reservas values (8, 4, 5, 200, to_date('2017-06-02', 'YYYY-MM-DD'), to_date('2017-06-03', 'YYYY-MM-DD'), 196589789, 'Dri Agnes Aimee', 'dri.aimee@gmail.com', 'M',33937465826, 'Franca'); --caso com o anterior
insert into v_reservas values (9, 2, 1, 200, to_date('2017-05-31', 'YYYY-MM-DD'), to_date('2017-06-02', 'YYYY-MM-DD'), 108567578, 'Roberto Pereira', 'roberto@hotmail.com' , 'M', 923846485,  'Portugal'); --caso com o 1º
insert into v_reservas values (10, 8, 8, 400, to_date('2017-06-16', 'YYYY-MM-DD'), to_date('2017-06-17', 'YYYY-MM-DD'), 197857578, 'Ana Teixeira', 'anat@live.com.pt' , 'F', 922465456, 'Portugal'); --caso T3

--Inserir CheckOut/
insert into checkOut values (to_date('2017-06-02', 'YYYY-MM-DD'), 1, 5, 137597504, SEQ_CHECKOUT.NEXTVAL);
insert into checkOut values (to_date('2017-06-04', 'YYYY-MM-DD'), 2, 4, 174927495, SEQ_CHECKOUT.NEXTVAL);
insert into checkOut values (to_date('2017-06-06', 'YYYY-MM-DD'), 3, 3, 137597504, SEQ_CHECKOUT.NEXTVAL);
insert into checkOut values (to_date('2017-06-02', 'YYYY-MM-DD'), 4, 5, 137597504, SEQ_CHECKOUT.NEXTVAL);
insert into checkOut values (to_date('2017-06-04', 'YYYY-MM-DD'), 5, 5, 174927495, SEQ_CHECKOUT.NEXTVAL);
insert into checkOut values (to_date('2017-06-03', 'YYYY-MM-DD'), 6, 4, 174927495, SEQ_CHECKOUT.NEXTVAL);
insert into checkOut values (to_date('2017-06-04', 'YYYY-MM-DD'), 7, 4, 137597504, SEQ_CHECKOUT.NEXTVAL);

commit;