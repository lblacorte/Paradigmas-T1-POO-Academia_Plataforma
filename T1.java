package t1;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

// Classe de Conexão com o Banco de Dados
class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/academia";
    private static final String USER = "postgres";
    private static final String PASSWORD = "inter2005";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// Classe Aluno
class Aluno {
    private int id; //serial primary key
    private String cpf;
    private String nome;
    private String dataNascimento;

    public Aluno(int id, String cpf, String nome, String dataNascimento) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
}

// Classe Exercício
class Exercicio {
    private int id;
    private String nome;
    private String musculosTrabalhados;

    public Exercicio(int id, String nome, String musculosTrabalhados) {
        this.id = id;
        this.nome = nome;
        this.musculosTrabalhados = musculosTrabalhados;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMusculosTrabalhados() { return musculosTrabalhados; }
    public void setMusculosTrabalhados(String musculosTrabalhados) { this.musculosTrabalhados = musculosTrabalhados; }
}

// Classe Treino
class Treino {
    private String cpfAluno;
    private String nome;
    private int id;

    public Treino(String cpfAluno, String nome, int id) {
        this.cpfAluno = cpfAluno;
        this.nome = nome;
        this.id = id;
    }

    // Getters e Setters
    public String getCpfAluno() { return cpfAluno; }
    public void setCpfAluno(String cpfAluno) { this.cpfAluno = cpfAluno; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}

// Operações de Aluno
class AlunoDAO {
    public void inserirAluno(Aluno aluno) {
        String sql = "INSERT INTO Alunos (cpf, nome, data_nascimento) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aluno.getCpf());
            stmt.setString(2, aluno.getNome());
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    //altera aluno a partir do id
    public void alterarAluno(Aluno aluno) {
        String sql = "UPDATE Alunos SET cpf = ?, nome = ?, data_nascimento = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getCpf());
            stmt.setString(2, aluno.getNome());
            stmt.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            stmt.setInt(4, aluno.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void excluirAluno(String cpf) {
        String sql = "DELETE FROM Alunos WHERE cpf = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public Aluno buscarAlunoPorCpf(String cpf) {
        String sql = "SELECT * FROM Alunos WHERE cpf = ?";
        Aluno aluno = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                aluno = new Aluno(rs.getInt("id"), rs.getString("cpf"), rs.getString("nome"), rs.getDate("data_nascimento").toString());
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return aluno;
    }

    public Aluno buscarAlunoPorNome(String nome) {
        String sql = "SELECT * FROM Alunos WHERE nome = ?";
        Aluno aluno = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                aluno = new Aluno(rs.getInt("id"), rs.getString("cpf"), rs.getString("nome"), rs.getDate("data_nascimento").toString());
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return aluno;
    }

    public List<Aluno> listarAlunos() {
        String sql = "SELECT * FROM Alunos ORDER BY nome";
        List<Aluno> alunos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aluno aluno = new Aluno(rs.getInt("id"), rs.getString("cpf"), rs.getString("nome"), rs.getDate("data_nascimento").toString());
                alunos.add(aluno);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return alunos;
    }
}

// Operações de Exercício
class ExercicioDAO {
    public void inserirExercicio(Exercicio exercicio) {
        String sql = "INSERT INTO Exercicios (id, nome, musculos_trabalhados) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, exercicio.getId());
            stmt.setString(2, exercicio.getNome());
            stmt.setString(3, exercicio.getMusculosTrabalhados());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void alterarExercicio(Exercicio exercicio) {
        String sql = "UPDATE Exercicios SET nome = ?, musculos_trabalhados = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, exercicio.getNome());
            stmt.setString(2, exercicio.getMusculosTrabalhados());
            stmt.setInt(3, exercicio.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void excluirExercicio(int id) {
        String sql = "DELETE FROM Exercicios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public Exercicio buscarExercicioPorId(int id) {
        String sql = "SELECT * FROM Exercicios WHERE id = ?";
        Exercicio exercicio = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                exercicio = new Exercicio(rs.getInt("id"), rs.getString("nome"), rs.getString("musculos_trabalhados"));
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return exercicio;
    }

    public List<Exercicio> listarExercicios() {
        String sql = "SELECT * FROM Exercicios";
        List<Exercicio> exercicios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Exercicio exercicio = new Exercicio(rs.getInt("id"), rs.getString("nome"), rs.getString("musculos_trabalhados"));
                exercicios.add(exercicio);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return exercicios;
    }
}

// Classe Plano
class Plano {
    private int id;
    private String nome;
    private double preco;

    public Plano(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPreco() { return preco; }
    public void setValor(double preco) { this.preco = preco; }
}

//Operações de Plano
class PlanoDAO {
    public void inserirPlano(Plano plano) {
        String sql = "INSERT INTO Planos (id, nome, preco) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, plano.getId());
            stmt.setString(2, plano.getNome());
            stmt.setDouble(3, plano.getPreco());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void alterarPlano(Plano plano) {
        String sql = "UPDATE Planos SET nome = ?, preco = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plano.getNome());
            stmt.setDouble(2, plano.getPreco());
            stmt.setInt(3, plano.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void excluirPlano(int id) {
        String sql = "DELETE FROM Planos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public Plano buscarPlanoPorId(int id) {
        String sql = "SELECT * FROM Planos WHERE id = ?";
        Plano plano = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                plano = new Plano(rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco"));
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return plano;
    }

    public List<Plano> listarPlanos() {
        String sql = "SELECT * FROM Planos";
        List<Plano> planos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Plano plano = new Plano(rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco"));
                planos.add(plano);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return planos;
    }
}

//Classe PlanoAluno
class PlanoAluno {
    private String cpfAluno;
    private int idPlano;
    private Date dataInicio;
    private String numeroCartaoCredito;
    private Date vencimentoCartaoCredito;
    private String cvvCartaoCredito;

    public PlanoAluno(String cpfAluno, int idPlano, Date dataInicio, String numeroCartaoCredito, Date vencimentoCartaoCredito, String cvvCartaoCredito) {
        this.cpfAluno = cpfAluno;
        this.idPlano = idPlano;
        this.dataInicio = dataInicio;
        this.numeroCartaoCredito = numeroCartaoCredito;
        this.vencimentoCartaoCredito = vencimentoCartaoCredito;
        this.cvvCartaoCredito = cvvCartaoCredito;
    }

    // Getters e Setters
    public String getCpfAluno() { return cpfAluno; }
    public void setCpfAluno(String cpfAluno) { this.cpfAluno = cpfAluno; }

    public int getIdPlano() { return idPlano; }
    public void setIdPlano(int idPlano) { this.idPlano = idPlano; }

    public Date getDataInicio() { return dataInicio; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }

    public String getNumeroCartaoCredito() { return numeroCartaoCredito; }
    public void setNumeroCartaoCredito(String numeroCartaoCredito) { this.numeroCartaoCredito = numeroCartaoCredito; }

    public Date getVencimentoCartaoCredito() { return vencimentoCartaoCredito; }
    public void setVencimentoCartaoCredito(Date vencimentoCartaoCredito) { this.vencimentoCartaoCredito = vencimentoCartaoCredito; }

    public String getCvvCartaoCredito() { return cvvCartaoCredito; }
    public void setCvvCartaoCredito(String cvvCartaoCredito) { this.cvvCartaoCredito = cvvCartaoCredito; }
}

class PlanoAlunoDAO{
    //Cadastrar plano de aluno
    public void cadastrarPlanoAluno(String cpfAluno, int idPlano, Date dataInicio, String numeroCartaoCredito, Date vencimentoCartaoCredito, String cvvCartaoCredito) {
        String sql = "INSERT INTO Aluno_Planos (cpf_aluno, id_plano, data_inicio, numero_cartao_credito, vencimento_cartao_credito, cvv_cartao_credito) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfAluno);
            stmt.setInt(2, idPlano);
            stmt.setDate(3, dataInicio);
            stmt.setString(4, numeroCartaoCredito);
            stmt.setDate(5, vencimentoCartaoCredito);
            stmt.setString(6, cvvCartaoCredito);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void editarPlanoAluno(String cpfAluno, int idPlano, Date dataInicio, String numeroCartaoCredito, Date vencimentoCartaoCredito, String cvvCartaoCredito) {
        String sql = "UPDATE Aluno_Planos SET data_inicio = ?, numero_cartao_credito = ?, vencimento_cartao_credito = ?, cvv_cartao_credito = ? WHERE cpf_aluno = ? AND id_plano = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, dataInicio);
            stmt.setString(2, numeroCartaoCredito);
            stmt.setDate(3, vencimentoCartaoCredito);
            stmt.setString(4, cvvCartaoCredito);
            stmt.setString(5, cpfAluno);
            stmt.setInt(6, idPlano);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void excluirPlanoAluno(String cpfAluno, int idPlano) {
        String sql = "DELETE FROM Aluno_Planos WHERE cpf_aluno = ? AND id_plano = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfAluno);
            stmt.setInt(2, idPlano);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    //listar planos de um aluno
    public List<PlanoAluno> listarPlanosAluno(String cpfAluno) {
        String sql = "SELECT * FROM Aluno_Planos WHERE cpf_aluno = ?";
        List<PlanoAluno> planosAluno = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PlanoAluno planoAluno = new PlanoAluno(rs.getString("cpf_aluno"), rs.getInt("id_plano"), rs.getDate("data_inicio"), rs.getString("numero_cartao_credito"), rs.getDate("vencimento_cartao_credito"), rs.getString("cvv_cartao_credito"));
                planosAluno.add(planoAluno);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return planosAluno;
    }
}

class ExercicioTreino {
    private int id;
    private int idTreino;
    private int idExercicio;
    private int numeroSeries;
    private int repeticoesMin;
    private int repeticoesMax;
    private double cargaKg;
    private double tempoDescansoMin;

    public ExercicioTreino(int id, int idTreino, int idExercicio, int numeroSeries, int repeticoesMin, int repeticoesMax, double cargaKg, double tempoDescansoMin) {
        this.id = id;
        this.idTreino = idTreino;
        this.idExercicio = idExercicio;
        this.numeroSeries = numeroSeries;
        this.repeticoesMin = repeticoesMin;
        this.repeticoesMax = repeticoesMax;
        this.cargaKg = cargaKg;
        this.tempoDescansoMin = tempoDescansoMin;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTreino() {
        return idTreino;
    }

    public void setIdTreino(int idTreino) {
        this.idTreino = idTreino;
    }

    public int getIdExercicio() {
        return idExercicio;
    }

    public void setIdExercicio(int idExercicio) {
        this.idExercicio = idExercicio;
    }

    public int getNumeroSeries() {
        return numeroSeries;
    }

    public void setNumeroSeries(int numeroSeries) {
        this.numeroSeries = numeroSeries;
    }

    public int getRepeticoesMin() {
        return repeticoesMin;
    }

    public void setRepeticoesMin(int repeticoesMin) {
        this.repeticoesMin = repeticoesMin;
    }

    public int getRepeticoesMax() {
        return repeticoesMax;
    }

    public void setRepeticoesMax(int repeticoesMax) {
        this.repeticoesMax = repeticoesMax;
    }

    public double getCargaKg() {
        return cargaKg;
    }

    public void setCargaKg(double cargaKg) {
        this.cargaKg = cargaKg;
    }

    public double getTempoDescansoMin() {
        return tempoDescansoMin;
    }

    public void setTempoDescansoMin(double tempoDescansoMin) {
        this.tempoDescansoMin = tempoDescansoMin;
    }
}

// Operações de ExercicioTreino
class ExercicioTreinoDAO {
    public void cadastrarExercicioTreino(ExercicioTreino exercicioTreino) {
        String sql = "INSERT INTO Treino_Exercicios (id_treino, id_exercicio, numero_series, repeticoes_min, repeticoes_max, carga_kg, tempo_descanso_min) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exercicioTreino.getIdTreino());
            stmt.setInt(2, exercicioTreino.getIdExercicio());
            stmt.setInt(3, exercicioTreino.getNumeroSeries());
            stmt.setInt(4, exercicioTreino.getRepeticoesMin());
            stmt.setInt(5, exercicioTreino.getRepeticoesMax());
            stmt.setDouble(6, exercicioTreino.getCargaKg());
            stmt.setDouble(7, exercicioTreino.getTempoDescansoMin());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int buscarIdExercicioPorIdPK(int id) {
        String sql = "SELECT id_exercicio FROM Treino_Exercicios WHERE id = ?";
        int idExercicio = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idExercicio = rs.getInt("id_exercicio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idExercicio;
    }

    public void buscarExercicioTreino(int idTreino, int idExercicio) {
        String sql = "SELECT * FROM Treino_Exercicios WHERE id_treino = ? AND id_exercicio = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTreino);
            stmt.setInt(2, idExercicio);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int numeroSeries = rs.getInt("numero_series");
                int repeticoesMin = rs.getInt("repeticoes_min");
                int repeticoesMax = rs.getInt("repeticoes_max");
                double cargaKg = rs.getDouble("carga_kg");
                double tempoDescansoMin = rs.getDouble("tempo_descanso_min");
                ExercicioTreino exercicioTreino = new ExercicioTreino(id, idTreino, idExercicio, numeroSeries, repeticoesMin, repeticoesMax, cargaKg, tempoDescansoMin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluirExercicioTreino(int id) {
        String sql = "DELETE FROM Treino_Exercicios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editarExercicioTreino(ExercicioTreino exercicioTreino) {
        String sql = "UPDATE Treino_Exercicios SET numero_series = ?, repeticoes_min = ?, repeticoes_max = ?, carga_kg = ?, tempo_descanso_min = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exercicioTreino.getNumeroSeries());
            stmt.setInt(2, exercicioTreino.getRepeticoesMin());
            stmt.setInt(3, exercicioTreino.getRepeticoesMax());
            stmt.setDouble(4, exercicioTreino.getCargaKg());
            stmt.setDouble(5, exercicioTreino.getTempoDescansoMin());
            stmt.setInt(6, exercicioTreino.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ExercicioTreino> listarExerciciosTreino(int idTreino) {
        String sql = "SELECT * FROM Treino_Exercicios WHERE id_treino = ?";
        List<ExercicioTreino> exerciciosTreino = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTreino);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int idExercicio = rs.getInt("id_exercicio");
                int numeroSeries = rs.getInt("numero_series");
                int repeticoesMin = rs.getInt("repeticoes_min");
                int repeticoesMax = rs.getInt("repeticoes_max");
                double cargaKg = rs.getDouble("carga_kg");
                double tempoDescansoMin = rs.getDouble("tempo_descanso_min");
                ExercicioTreino exercicioTreino = new ExercicioTreino(id, idTreino, idExercicio, numeroSeries, repeticoesMin, repeticoesMax, cargaKg, tempoDescansoMin);
                exerciciosTreino.add(exercicioTreino);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exerciciosTreino;
    }
}

//Cadastrar treino de um aluno
class TreinoDAO {
    public void cadastrarTreino(Treino treino) {
        String sql = "INSERT INTO Treinos (cpf_aluno, nome) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, treino.getCpfAluno());
            stmt.setString(2, treino.getNome());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    //editar treino
    public void editarTreino(Treino treino) {
        String sql = "UPDATE Treinos SET nome = ? WHERE cpf_aluno = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, treino.getNome());
            stmt.setString(2, treino.getCpfAluno());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    public void excluirTreino(int id) {
        String sql = "DELETE FROM Treinos WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    //buscar nome do treino por id
    public String buscarNomeTreinoPorId(int id) {
        String sql = "SELECT nome FROM Treinos WHERE id = ?";
        String nome = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nome = rs.getString("nome");
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return nome;
    }
    //listar treinos
    public List<Treino> listarTreinos(String cpfAluno) {
        String sql = "SELECT * FROM Treinos WHERE cpf_aluno = ?";
        List<Treino> treinos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Treino treino = new Treino(rs.getString("cpf_aluno"), rs.getString("nome"), rs.getInt("id"));
                treinos.add(treino);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return treinos;
    }
}

class HistoricoTreino {
    private int id;
    private int idTreino;
    private Date dataTreino;
    private boolean treinoConcluido;

    public HistoricoTreino(int id, int idTreino, Date dataTreino, boolean treinoConcluido) {
        this.id = id;
        this.idTreino = idTreino;
        this.dataTreino = dataTreino;
        this.treinoConcluido = treinoConcluido;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdTreino() { return idTreino; }
    public void setIdTreino(int idTreino) { this.idTreino = idTreino; }

    public Date getDataTreino() { return dataTreino; }
    public void setDataTreino(Date dataTreino) { this.dataTreino = dataTreino; }

    public boolean isTreinoConcluido() { return treinoConcluido; }
    public void setTreinoConcluido(boolean treinoConcluido) { this.treinoConcluido = treinoConcluido; }
}

//Atualizar histórico de treinos de um aluno
class HistoricoTreinoDAO {
    public void cadastrarHistoricoTreino(int idTreino, Date dataTreino, boolean treinoConcluido) {
        String sql = "INSERT INTO Historico_Treinos_Realizados (id_treino, data_treino, treino_concluido) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTreino);
            stmt.setDate(2, dataTreino);
            stmt.setBoolean(3, treinoConcluido);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void excluirHistoricoTreino(int id) {
        String sql = "DELETE FROM Historico_Treinos_Realizados WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    //listar histórico de treinos de um cpf de aluno
    public List<HistoricoTreino> listarHistoricoTreinos(String cpfAluno) {
        String sql = "SELECT * FROM Historico_Treinos_Realizados WHERE id_treino IN (SELECT id FROM Treinos WHERE cpf_aluno = ?)";
        List<HistoricoTreino> historicoTreinos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HistoricoTreino historicoTreino = new HistoricoTreino(rs.getInt("id"), rs.getInt("id_treino"), rs.getDate("data_treino"), rs.getBoolean("treino_concluido"));
                historicoTreinos.add(historicoTreino);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return historicoTreinos;
    }
    //pegar o id pk da última linha da tabela historico_treino e retorná-lo
    public int getIdUltimoHistoricoTreino() {
        String sql = "SELECT id FROM Historico_Treinos_Realizados ORDER BY id DESC LIMIT 1";
        int id = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return id;
    }
    //buscar data do treino por id
    public Date buscarDataTreinoPorId(int id) {
        String sql = "SELECT data_treino FROM Historico_Treinos_Realizados WHERE id = ?";
        Date dataTreino = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dataTreino = rs.getDate("data_treino");
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return dataTreino;
    }
    //editar histórico de treino
    public void editarHistoricoTreino(int id, Date dataTreino, boolean treinoConcluido) {
        String sql = "UPDATE Historico_Treinos_Realizados SET data_treino = ?, treino_concluido = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, dataTreino);
            stmt.setBoolean(2, treinoConcluido);
            stmt.setInt(3, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}

class HistoricoExercicio {
    private int id;
    private int idTreinoRealizado;
    private int idExercicio;
    private BigDecimal acrescimoPeso;
    private boolean exercicioConcluido;

    public HistoricoExercicio(int id, int idTreinoRealizado, int idExercicio, BigDecimal acrescimoPeso, boolean exercicioConcluido) {
        this.id = id;
        this.idTreinoRealizado = idTreinoRealizado;
        this.idExercicio = idExercicio;
        this.acrescimoPeso = acrescimoPeso;
        this.exercicioConcluido = exercicioConcluido;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdTreinoRealizado() { return idTreinoRealizado; }
    public void setIdTreinoRealizado(int idTreinoRealizado) { this.idTreinoRealizado = idTreinoRealizado; }

    public int getIdExercicio() { return idExercicio; }
    public void setIdExercicio(int idExercicio) { this.idExercicio = idExercicio; }

    public BigDecimal getAcrescimoPeso() { return acrescimoPeso; }
    public void setAcrescimoPeso(BigDecimal acrescimoPeso) { this.acrescimoPeso = acrescimoPeso; }

    public boolean isExercicioConcluido() { return exercicioConcluido; }
    public void setExercicioConcluido(boolean exercicioConcluido) { this.exercicioConcluido = exercicioConcluido; }
}

class HistoricoExercicioDAO {
    public void cadastrarHistoricoExercicio(int idTreinoRealizado, int idExercicio, BigDecimal acrescimoPeso, boolean exercicioConcluido) {
        String sql = "INSERT INTO Historico_Exercicios_Realizados (id_treino_realizado, id_exercicio, acrescimo_peso, exercicio_concluido) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTreinoRealizado);
            stmt.setInt(2, idExercicio);
            stmt.setBigDecimal(3, acrescimoPeso);
            stmt.setBoolean(4, exercicioConcluido);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void excluirHistoricoExercicio(int id) {
        String sql = "DELETE FROM Historico_Exercicios_Realizados WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public List<HistoricoExercicio> listarHistoricoExercicios(int idTreinoRealizado) {
        String sql = "SELECT * FROM Historico_Exercicios_Realizados WHERE id_treino_realizado = ?";
        List<HistoricoExercicio> historicoExercicios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTreinoRealizado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HistoricoExercicio historicoExercicio = new HistoricoExercicio(rs.getInt("id"), rs.getInt("id_treino_realizado"), rs.getInt("id_exercicio"), rs.getBigDecimal("acrescimo_peso"), rs.getBoolean("exercicio_concluido"));
                historicoExercicios.add(historicoExercicio);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return historicoExercicios;
    }
    //Listar histórico de um exercício
    public List<HistoricoExercicio> listarHistoricoExercicio(int idExercicio) {
        String sql = "SELECT * FROM Historico_Exercicios_Realizados WHERE id_exercicio = ?";
        List<HistoricoExercicio> historicoExercicios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idExercicio);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HistoricoExercicio historicoExercicio = new HistoricoExercicio(rs.getInt("id"), rs.getInt("id_treino_realizado"), rs.getInt("id_exercicio"), rs.getBigDecimal("acrescimo_peso"), rs.getBoolean("exercicio_concluido"));
                historicoExercicios.add(historicoExercicio);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }

        return historicoExercicios;
    }
    //editar histórico de exercício
    public void editarHistoricoExercicio(int id, BigDecimal acrescimoPeso, boolean exercicioConcluido) {
        String sql = "UPDATE Historico_Exercicios_Realizados SET acrescimo_peso = ?, exercicio_concluido = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, acrescimoPeso);
            stmt.setBoolean(2, exercicioConcluido);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}

//Clique em excluir aluno
class InterfaceExcluirAluno {
    private InterfaceVerAlunos telaAnterior;
    private Aluno aluno;

    public InterfaceExcluirAluno(InterfaceVerAlunos telaAnterior, Aluno aluno) {
        this.telaAnterior = telaAnterior;
        this.aluno = aluno;
    }

    public void criarInterface() {
        JFrame frame = new JFrame("Excluir Aluno");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);

        JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfTextField = new JTextField(10);
        cpfTextField.setText(aluno.getCpf());

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfTextField.getText();
                AlunoDAO alunoDAO = new AlunoDAO();
                alunoDAO.excluirAluno(cpf);

                // Atualizar a tela anterior
                telaAnterior.carregarAlunos();

                // Mensagem de sucesso
                frame.dispose();
                JOptionPane.showMessageDialog(null, "Aluno excluído com sucesso!");
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(cpfLabel);
        panel.add(cpfTextField);
        panel.add(confirmarButton);
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.getContentPane().setLayout(new FlowLayout());

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}

//Clique em editar aluno
class InterfaceEditarAluno {
    private InterfaceVerAlunos telaAnterior;
    private Aluno aluno;

    public InterfaceEditarAluno(InterfaceVerAlunos telaAnterior, Aluno aluno) {
        this.telaAnterior = telaAnterior;
        this.aluno = aluno;
    }

    public void criarInterface() {
        JFrame frame = new JFrame("Editar Aluno");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 200);

        JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfTextField = new JTextField(10);
        cpfTextField.setText(aluno.getCpf());
        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeTextField = new JTextField(20);
        nomeTextField.setText(aluno.getNome());
        JLabel dataNascimentoLabel = new JLabel("Data de Nascimento:");
        JTextField dataNascimentoTextField = new JTextField(10);
        dataNascimentoTextField.setText(aluno.getDataNascimento());

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfTextField.getText();
                String nome = nomeTextField.getText();
                String dataNascimento = dataNascimentoTextField.getText();
                int id = aluno.getId();

                Aluno aluno = new Aluno(id, cpf, nome, dataNascimento);
                AlunoDAO alunoDAO = new AlunoDAO();
                alunoDAO.alterarAluno(aluno);

                // Atualizar a tela anterior
                telaAnterior.carregarAlunos();

                // Mensagem de sucesso
                frame.dispose();
                JOptionPane.showMessageDialog(null, "Aluno alterado com sucesso!");
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(cpfLabel);
        panel.add(cpfTextField);
        panel.add(nomeLabel);
        panel.add(nomeTextField);
        panel.add(dataNascimentoLabel);
        panel.add(dataNascimentoTextField);
        panel.add(confirmarButton);
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Clique em cadastrar aluno
class InterfaceCadastrarAluno {
    private InterfaceVerAlunos telaAnterior;

    public InterfaceCadastrarAluno(InterfaceVerAlunos telaAnterior) {
        this.telaAnterior = telaAnterior;
    }

    public void criarInterface() {
        // Create the main frame
        JFrame frame = new JFrame("Cadastrar Aluno");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 200);

        JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfTextField = new JTextField(10);
        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeTextField = new JTextField(20);
        JLabel dataNascimentoLabel = new JLabel("Data de Nascimento:");
        JTextField dataNascimentoTextField = new JTextField(10);
        dataNascimentoTextField.setText("yyyy-mm-dd");

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfTextField.getText();
                String nome = nomeTextField.getText();
                String dataNascimento = dataNascimentoTextField.getText();

                Aluno aluno = new Aluno(0, cpf, nome, dataNascimento);
                AlunoDAO alunoDAO = new AlunoDAO();
                alunoDAO.inserirAluno(aluno);

                // Atualizar a tela anterior
                telaAnterior.carregarAlunos();

                // Mensagem de sucesso
                frame.dispose();
                JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(cpfLabel);
        panel.add(cpfTextField);
        panel.add(nomeLabel);
        panel.add(nomeTextField);
        panel.add(dataNascimentoLabel);
        panel.add(dataNascimentoTextField);
        panel.add(confirmarButton);
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Clique em cadastrar plano
class InterfaceCadastrarPlano {
    public void criarInterface() {
        JFrame frame = new JFrame("Cadastrar Plano");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 200);

        JLabel idLabel = new JLabel("ID:");
        JTextField idTextField = new JTextField(10);
        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeTextField = new JTextField(20);
        JLabel precoLabel = new JLabel("Preço:");
        JTextField precoTextField = new JTextField(10);

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(idTextField.getText());
                String nome = nomeTextField.getText();
                double preco = Double.parseDouble(precoTextField.getText());

                Plano plano = new Plano(id, nome, preco);
                PlanoDAO planoDAO = new PlanoDAO();
                planoDAO.inserirPlano(plano);
                //mensagem de sucesso
                JOptionPane.showMessageDialog(null, "Plano cadastrado com sucesso!");
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                //voltar para a tela do instrutor
                InterfaceInstrutor interfaceInstrutor = new InterfaceInstrutor();
                interfaceInstrutor.criarInterface();
            }
        });

        JPanel panel = new JPanel();
        panel.add(idLabel);
        panel.add(idTextField);
        panel.add(nomeLabel);
        panel.add(nomeTextField);
        panel.add(precoLabel);
        panel.add(precoTextField);
        panel.add(confirmarButton);
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//EDITAR EXERCÍCIOS DE TREINO
class InterfaceEditarExercicioTreino {
    private JTable tabela;
    private DefaultTableModel modelo;

    public InterfaceEditarExercicioTreino(JTable tabela, DefaultTableModel modelo) {
        this.tabela = tabela;
        this.modelo = modelo;
    }

    public void criarInterface(ExercicioTreino exercicioTreino) {
        JFrame frame = new JFrame("Editar Exercício de Treino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JLabel idExercicioLabel = new JLabel("ID do Exercício:");
        JTextField idExercicioTextField = new JTextField(10);
        idExercicioTextField.setText(String.valueOf(exercicioTreino.getIdExercicio()));
        JLabel numeroSeriesLabel = new JLabel("Número de Séries:");
        JTextField numeroSeriesTextField = new JTextField(10);
        numeroSeriesTextField.setText(String.valueOf(exercicioTreino.getNumeroSeries()));
        JLabel repeticoesMinLabel = new JLabel("Repetições Mínimas:");
        JTextField repeticoesMinTextField = new JTextField(10);
        repeticoesMinTextField.setText(String.valueOf(exercicioTreino.getRepeticoesMin()));
        JLabel repeticoesMaxLabel = new JLabel("Repetições Máximas:");
        JTextField repeticoesMaxTextField = new JTextField(10);
        repeticoesMaxTextField.setText(String.valueOf(exercicioTreino.getRepeticoesMax()));
        JLabel cargaKgLabel = new JLabel("Carga (kg):");
        JTextField cargaKgTextField = new JTextField(10);
        cargaKgTextField.setText(String.valueOf(exercicioTreino.getCargaKg()));
        JLabel tempoDescansoMinLabel = new JLabel("Tempo de Descanso (min):");
        JTextField tempoDescansoMinTextField = new JTextField(10);
        tempoDescansoMinTextField.setText(String.valueOf(exercicioTreino.getTempoDescansoMin()));

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = exercicioTreino.getId();
                int idTreino = exercicioTreino.getIdTreino();
                int idExercicio = Integer.parseInt(idExercicioTextField.getText());
                int numeroSeries = Integer.parseInt(numeroSeriesTextField.getText());
                int repeticoesMin = Integer.parseInt(repeticoesMinTextField.getText());
                int repeticoesMax = Integer.parseInt(repeticoesMaxTextField.getText());
                double cargaKg = Double.parseDouble(cargaKgTextField.getText());
                double tempoDescansoMin = Double.parseDouble(tempoDescansoMinTextField.getText());

                ExercicioTreino exercicioTreinoAtualizado = new ExercicioTreino(id, idTreino, idExercicio, numeroSeries, repeticoesMin, repeticoesMax, cargaKg, tempoDescansoMin);
                ExercicioTreinoDAO exercicioTreinoDAO = new ExercicioTreinoDAO();
                exercicioTreinoDAO.editarExercicioTreino(exercicioTreinoAtualizado);

                // Atualizar a linha da tabela com os novos dados
                int linhaSelecionada = tabela.getSelectedRow();
                modelo.setValueAt(idExercicio, linhaSelecionada, 2);
                modelo.setValueAt(numeroSeries, linhaSelecionada, 3);
                modelo.setValueAt(repeticoesMin, linhaSelecionada, 4);
                modelo.setValueAt(repeticoesMax, linhaSelecionada, 5);
                modelo.setValueAt(cargaKg, linhaSelecionada, 6);
                modelo.setValueAt(tempoDescansoMin, linhaSelecionada, 7);

                JOptionPane.showMessageDialog(null, "Exercício de treino alterado com sucesso!");
                frame.dispose();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5));

        panel.add(idExercicioLabel);
        panel.add(idExercicioTextField);
        panel.add(numeroSeriesLabel);
        panel.add(numeroSeriesTextField);
        panel.add(repeticoesMinLabel);
        panel.add(repeticoesMinTextField);
        panel.add(repeticoesMaxLabel);
        panel.add(repeticoesMaxTextField);
        panel.add(cargaKgLabel);
        panel.add(cargaKgTextField);
        panel.add(tempoDescansoMinLabel);
        panel.add(tempoDescansoMinTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(confirmarButton);
        buttonPanel.add(voltarButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
    
        frame.setVisible(true);
    }
}

//Clique em cadastrar exercício de treino
class InterfaceCadastrarExercicioTreino {
    private JTable tabela;
    private DefaultTableModel modelo;

    public InterfaceCadastrarExercicioTreino(JTable tabela, DefaultTableModel modelo) {
        this.tabela = tabela;
        this.modelo = modelo;
    }

    public void criarInterface(Treino treino) {
        JFrame frame = new JFrame("Cadastrar Exercício de Treino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JLabel idExercicioLabel = new JLabel("ID do Exercício:");
        JTextField idExercicioTextField = new JTextField(10);
        JLabel numeroSeriesLabel = new JLabel("Número de Séries:");
        JTextField numeroSeriesTextField = new JTextField(10);
        JLabel repeticoesMinLabel = new JLabel("Repetições Mínimas:");
        JTextField repeticoesMinTextField = new JTextField(10);
        JLabel repeticoesMaxLabel = new JLabel("Repetições Máximas:");
        JTextField repeticoesMaxTextField = new JTextField(10);
        JLabel cargaKgLabel = new JLabel("Carga (kg):");
        JTextField cargaKgTextField = new JTextField(10);
        JLabel tempoDescansoMinLabel = new JLabel("Tempo de Descanso (min):");
        JTextField tempoDescansoMinTextField = new JTextField(10);

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idExercicio = Integer.parseInt(idExercicioTextField.getText());
                int numeroSeries = Integer.parseInt(numeroSeriesTextField.getText());
                int repeticoesMin = Integer.parseInt(repeticoesMinTextField.getText());
                int repeticoesMax = Integer.parseInt(repeticoesMaxTextField.getText());
                double cargaKg = Double.parseDouble(cargaKgTextField.getText());
                double tempoDescansoMin = Double.parseDouble(tempoDescansoMinTextField.getText());

                ExercicioTreino exercicioTreino = new ExercicioTreino(0, treino.getId(), idExercicio, numeroSeries, repeticoesMin, repeticoesMax, cargaKg, tempoDescansoMin);
                ExercicioTreinoDAO exercicioTreinoDAO = new ExercicioTreinoDAO();
                exercicioTreinoDAO.cadastrarExercicioTreino(exercicioTreino);

                // Adicionar nova linha à tabela
                ExercicioDAO exercicioDAO = new ExercicioDAO();
                Exercicio exercicio = exercicioDAO.buscarExercicioPorId(idExercicio);
                Object[] linha = {
                    exercicioTreino.getId(), treino.getNome(), exercicio.getNome(),
                    numeroSeries, repeticoesMin, repeticoesMax, cargaKg, tempoDescansoMin
                };
                modelo.addRow(linha);

                JOptionPane.showMessageDialog(null, "Exercício de treino cadastrado com sucesso!");
                frame.dispose();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5));

        panel.add(idExercicioLabel);
        panel.add(idExercicioTextField);
        panel.add(numeroSeriesLabel);
        panel.add(numeroSeriesTextField);
        panel.add(repeticoesMinLabel);
        panel.add(repeticoesMinTextField);
        panel.add(repeticoesMaxLabel);
        panel.add(repeticoesMaxTextField);
        panel.add(cargaKgLabel);
        panel.add(cargaKgTextField);
        panel.add(tempoDescansoMinLabel);
        panel.add(tempoDescansoMinTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(confirmarButton);
        buttonPanel.add(voltarButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}

//Excluir exercício de treino
class InterfaceExcluirExercicioTreino {
    private JTable tabela;
    private DefaultTableModel modelo;

    public InterfaceExcluirExercicioTreino(JTable tabela, DefaultTableModel modelo) {
        this.tabela = tabela;
        this.modelo = modelo;
    }

    public void criarInterface(ExercicioTreino exercicioTreino) {
        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o exercício de treino?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            ExercicioTreinoDAO exercicioTreinoDAO = new ExercicioTreinoDAO();
            exercicioTreinoDAO.excluirExercicioTreino(exercicioTreino.getId());

            // Remover a linha correspondente da tabela
            int linhaSelecionada = tabela.getSelectedRow();
            modelo.removeRow(linhaSelecionada);

            JOptionPane.showMessageDialog(null, "Exercício de treino excluído com sucesso!");
        }
    }
}

//CONFIGURAR EXERCÍCIOS DE TREINO
class InterfaceConfigurarExercicioTreino {
    public void criarInterface(Treino treino) {
        JFrame frame = new JFrame("Configurar Exercícios de Treino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1060, 600);

        String[] colunas = {
            "ID", "Nome do Treino", "Nome do Exercício", "Número de Séries",
            "Repetições Mínimas", "Repetições Máximas", "Carga (kg)", "Tempo de Descanso (min)"
        };
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Preencher a tabela com os exercícios do treino
        ExercicioTreinoDAO exercicioTreinoDAO = new ExercicioTreinoDAO();
        ExercicioDAO exercicioDAO = new ExercicioDAO();
        TreinoDAO treinoDAO = new TreinoDAO();
        List<ExercicioTreino> exerciciosTreino = exercicioTreinoDAO.listarExerciciosTreino(treino.getId());
        for (ExercicioTreino exercicioTreino : exerciciosTreino) {
            int idExercicio = exercicioTreino.getIdExercicio();
            int idTreino = exercicioTreino.getIdTreino();
            String nomeTreino = treinoDAO.buscarNomeTreinoPorId(idTreino);
            Exercicio exercicio = exercicioDAO.buscarExercicioPorId(idExercicio);
            Object[] linha = {
                exercicioTreino.getId(), nomeTreino, exercicio.getNome(),
                exercicioTreino.getNumeroSeries(), exercicioTreino.getRepeticoesMin(), exercicioTreino.getRepeticoesMax(),
                exercicioTreino.getCargaKg(), exercicioTreino.getTempoDescansoMin()
            };
            modelo.addRow(linha);
        }

        // Criar a tabela
        JTable tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);

        // Ajustar a largura das colunas para 130 pixels
        ajustarLarguraColunas(tabela, 130);

        // Criar botões
        JButton editarButton = new JButton("Editar");
        JButton excluirButton = new JButton("Excluir");
        JButton cadastrarButton = new JButton("Cadastrar Novo Exercício");
        JButton voltarButton = new JButton("VOLTAR");

        // Adicionar ação aos botões
        editarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    ExercicioTreino exercicioTreino = exerciciosTreino.get(linhaSelecionada);
                    InterfaceEditarExercicioTreino interfaceEditarExercicioTreino = new InterfaceEditarExercicioTreino(tabela, modelo);
                    interfaceEditarExercicioTreino.criarInterface(exercicioTreino);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione um exercício para editar!");
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    ExercicioTreino exercicioTreino = exerciciosTreino.get(linhaSelecionada);
                    exercicioTreinoDAO.excluirExercicioTreino(exercicioTreino.getId());
                    modelo.removeRow(linhaSelecionada);
                    JOptionPane.showMessageDialog(null, "Exercício excluído com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione um exercício para excluir!");
                }
            }
        });

        cadastrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceCadastrarExercicioTreino interfaceCadastrarExercicioTreino = new InterfaceCadastrarExercicioTreino(tabela, modelo);
                interfaceCadastrarExercicioTreino.criarInterface(treino);
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Adicionar componentes ao painel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editarButton);
        buttonPanel.add(excluirButton);
        buttonPanel.add(cadastrarButton);
        buttonPanel.add(voltarButton);

        // Adicionar scrollPane e buttonPanel ao frame
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Centralizar o frame na tela
        frame.setLocationRelativeTo(null);
        // Exibir o frame
        frame.setVisible(true);
    }

    private void ajustarLarguraColunas(JTable tabela, int largura) {
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            TableColumn coluna = tabela.getColumnModel().getColumn(i);
            coluna.setPreferredWidth(largura);
            coluna.setMinWidth(largura);
            coluna.setMaxWidth(largura);
        }
    }
}

class InterfaceCadastrarTreino {
    private JFrame frame;
    private JTable tabelaTreinos;
    private DefaultTableModel modeloTabela;

    public InterfaceCadastrarTreino(JFrame frame, JTable tabelaTreinos, DefaultTableModel modeloTabela) {
        this.frame = frame;
        this.tabelaTreinos = tabelaTreinos;
        this.modeloTabela = modeloTabela;
    }

    public void criarInterface(Aluno aluno) {
        JDialog dialog = new JDialog(frame, "Cadastrar Treino", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new FlowLayout());

        JLabel nomeLabel = new JLabel("Nome do Treino:");
        JTextField nomeTextField = new JTextField(20);
        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        panel.add(nomeLabel);
        panel.add(nomeTextField);
        panel.add(confirmarButton);
        panel.add(voltarButton);

        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeTextField.getText();
                Treino treino = new Treino(aluno.getCpf(), nome, 0);
                TreinoDAO treinoDAO = new TreinoDAO();
                treinoDAO.cadastrarTreino(treino);

                // Update the table
                modeloTabela.setRowCount(0); // Clear table
                List<Treino> treinos = treinoDAO.listarTreinos(aluno.getCpf());
                for (Treino t : treinos) {
                    modeloTabela.addRow(new Object[]{t.getId(), t.getNome()});
                }

                JOptionPane.showMessageDialog(dialog, "Treino cadastrado com sucesso!");
                dialog.dispose();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}

//Cadastro de exercício
class InterfaceCadastrarExercicio {
    public void criarInterface() {
        JFrame frame = new JFrame("Cadastrar Exercício");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeTextField = new JTextField(20);
        JLabel musculosTrabalhadosLabel = new JLabel("Músculos Trabalhados:");
        JTextField musculosTrabalhadosTextField = new JTextField(20);

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = nomeTextField.getText();
                String musculosTrabalhados = musculosTrabalhadosTextField.getText();
                Exercicio exercicio = new Exercicio(0, nome, musculosTrabalhados);
                ExercicioDAO exercicioDAO = new ExercicioDAO();
                exercicioDAO.inserirExercicio(exercicio);
                // mensagem de sucesso
                JOptionPane.showMessageDialog(null, "Exercício cadastrado com sucesso!");
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceInstrutor interfaceInstrutor = new InterfaceInstrutor();
                interfaceInstrutor.criarInterface();
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        nomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomeTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        musculosTrabalhadosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        musculosTrabalhadosTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        voltarButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(20));
        panel.add(nomeLabel);
        panel.add(nomeTextField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(musculosTrabalhadosLabel);
        panel.add(musculosTrabalhadosTextField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(confirmarButton);
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Editar treino
class InterfaceEditarTreino {
    private JFrame frame;
    private JTable tabelaTreinos;
    private DefaultTableModel modeloTabela;

    public InterfaceEditarTreino(JFrame frame, JTable tabelaTreinos, DefaultTableModel modeloTabela) {
        this.frame = frame;
        this.tabelaTreinos = tabelaTreinos;
        this.modeloTabela = modeloTabela;
    }

    public void criarInterface(Treino treino) {
        JDialog dialog = new JDialog(frame, "Editar Treino", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new FlowLayout());

        JLabel nomeLabel = new JLabel("Nome do Treino:");
        JTextField nomeTextField = new JTextField(20);
        nomeTextField.setText(treino.getNome());

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        panel.add(nomeLabel);
        panel.add(nomeTextField);
        panel.add(confirmarButton);
        panel.add(voltarButton);

        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeTextField.getText();
                Treino treinoAtualizado = new Treino(treino.getCpfAluno(), nome, treino.getId());
                TreinoDAO treinoDAO = new TreinoDAO();
                treinoDAO.editarTreino(treinoAtualizado);

                modeloTabela.setRowCount(0); // Clear table
                List<Treino> treinos = treinoDAO.listarTreinos(treino.getCpfAluno());
                for (Treino t : treinos) {
                    modeloTabela.addRow(new Object[]{t.getId(), t.getNome()});
                }

                JOptionPane.showMessageDialog(dialog, "Treino editado com sucesso!");
                dialog.dispose();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}

//excluir treino
class InterfaceExcluirTreino {
    private JFrame frame;
    private JTable tabelaTreinos;
    private DefaultTableModel modeloTabela;

    public InterfaceExcluirTreino(JFrame frame, JTable tabelaTreinos, DefaultTableModel modeloTabela) {
        this.frame = frame;
        this.tabelaTreinos = tabelaTreinos;
        this.modeloTabela = modeloTabela;
    }

    public void criarInterface(Treino treino) {
        JDialog dialog = new JDialog(frame, "Excluir Treino", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new FlowLayout());

        JLabel idLabel = new JLabel("ID:");
        JTextField idTextField = new JTextField(10);
        idTextField.setText(Integer.toString(treino.getId()));
        idTextField.setEditable(false);

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        panel.add(idLabel);
        panel.add(idTextField);
        panel.add(confirmarButton);
        panel.add(voltarButton);

        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = treino.getId();
                TreinoDAO treinoDAO = new TreinoDAO();
                treinoDAO.excluirTreino(id);

                // Update the table
                modeloTabela.setRowCount(0); // Clear table
                List<Treino> treinos = treinoDAO.listarTreinos(treino.getCpfAluno());
                for (Treino t : treinos) {
                    modeloTabela.addRow(new Object[]{t.getId(), t.getNome()});
                }

                JOptionPane.showMessageDialog(dialog, "Treino excluído com sucesso!");
                dialog.dispose();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}

//CONFIGURAR TREINO DE ALUNO
class InterfaceConfigurarTreino {
    private JFrame frame;

    public InterfaceConfigurarTreino(JFrame frame) {
        this.frame = frame;
    }

    public void criarInterface(Aluno aluno) {
        JFrame frame = new JFrame("Configurar Treino de Aluno");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);

        String[] colunas = {"ID", "Nome"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Adicionar os treinos ao modelo
        TreinoDAO treinoDAO = new TreinoDAO();
        List<Treino> treinos = treinoDAO.listarTreinos(aluno.getCpf());
        for (Treino treino : treinos) {
            Object[] linha = {treino.getId(), treino.getNome()};
            modelo.addRow(linha);
        }

        // Criar a tabela com o modelo
        JTable tabela = new JTable(modelo);

        // Adicionar a tabela a um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabela);
        frame.add(scrollPane);

        //Criar botões de editar e excluir para cada treino
        JButton editarButton = new JButton("Editar");
        JButton excluirButton = new JButton("Excluir");
        JButton cadastrarButton = new JButton("Cadastrar");
        JButton configurarExercicioButton = new JButton("Configurar Exercícios");
        JButton voltarButton = new JButton("VOLTAR");

        editarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    int id = (int) modelo.getValueAt(linhaSelecionada, 0);
                    String nome = (String) modelo.getValueAt(linhaSelecionada, 1);

                    Treino treino = new Treino(aluno.getCpf(), nome, id);
                    InterfaceEditarTreino interfaceEditarTreino = new InterfaceEditarTreino(frame, tabela, modelo);
                    interfaceEditarTreino.criarInterface(treino);
                } else {
                    //mensagem de erro
                    JOptionPane.showMessageDialog(null, "Selecione um treino para editar!");
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    int id = (int) modelo.getValueAt(linhaSelecionada, 0);

                    Treino treino = new Treino(aluno.getCpf(), "", id);
                    InterfaceExcluirTreino interfaceExcluirTreino = new InterfaceExcluirTreino(frame, tabela, modelo);
                    interfaceExcluirTreino.criarInterface(treino);
                } else {
                    //mensagem de erro
                    JOptionPane.showMessageDialog(null, "Selecione um treino para excluir!");
                }
            }
        });

        cadastrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceCadastrarTreino interfaceCadastrarTreino = new InterfaceCadastrarTreino(frame, tabela, modelo);
                interfaceCadastrarTreino.criarInterface(aluno);
            }
        });

        configurarExercicioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    int id = (int) modelo.getValueAt(linhaSelecionada, 0);
                    String nome = (String) modelo.getValueAt(linhaSelecionada, 1);

                    Treino treino = new Treino(aluno.getCpf(), nome, id);
                    InterfaceConfigurarExercicioTreino interfaceConfigurarExercicioTreino = new InterfaceConfigurarExercicioTreino();
                    interfaceConfigurarExercicioTreino.criarInterface(treino);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione um treino para configurar os exercícios!");
                }
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.getContentPane().add(new JLabel("Treinos:"));
        frame.getContentPane().add(editarButton);
        frame.getContentPane().add(excluirButton);
        frame.getContentPane().add(cadastrarButton);
        frame.getContentPane().add(configurarExercicioButton);
        frame.getContentPane().add(voltarButton);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//CONFIGURAR PLANO DE ALUNO
class InterfaceConfigurarPlano {
    public void criarInterface(Aluno aluno) {
        // Create the main frame
        JFrame frame = new JFrame("Configurar Plano");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        // Tabela de planos
        String[] colunas = {"ID", "Nome", "Preço"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Adicionar os planos ao modelo
        PlanoDAO planoDAO = new PlanoDAO();
        List<Plano> planos = planoDAO.listarPlanos();
        for (Plano plano : planos) {
            Object[] linha = {plano.getId(), plano.getNome(), plano.getPreco()};
            modelo.addRow(linha);
        }

        // Criar a tabela com o modelo
        JTable tabela = new JTable(modelo);
        tabela.setPreferredScrollableViewportSize(new Dimension(450, 100));
        tabela.setFillsViewportHeight(true);

        // Definir a largura preferida das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200); // Nome
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100); // Preço

        // Adicionar a tabela a um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabela);

        //Tabela de planos do aluno
        //Título "Planos do Aluno"
        //Colunas: Nome, Data de Início, Número do Cartão de Crédito, Vencimento do Cartão de Crédito, CVV do Cartão de Crédito
        String[] colunasPlanoAluno = {"Nome", "Data Início", "Número Cartão", "Vencimento", "CVV"};
        DefaultTableModel modeloPlanoAluno = new DefaultTableModel(colunasPlanoAluno, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Adicionar os planos do aluno ao modelo
        PlanoAlunoDAO planoAlunoDAO = new PlanoAlunoDAO();
        List<PlanoAluno> planosAluno = planoAlunoDAO.listarPlanosAluno(aluno.getCpf());
        for (PlanoAluno planoAluno : planosAluno) {
            Plano plano = planoDAO.buscarPlanoPorId(planoAluno.getIdPlano());
            Object[] linha = {plano.getNome(), planoAluno.getDataInicio(), planoAluno.getNumeroCartaoCredito(), planoAluno.getVencimentoCartaoCredito(), planoAluno.getCvvCartaoCredito()};
            modeloPlanoAluno.addRow(linha);
        }

        // Criar a tabela com o modelo
        JTable tabelaPlanoAluno = new JTable(modeloPlanoAluno);
        tabelaPlanoAluno.setPreferredScrollableViewportSize(new Dimension(450, 100));
        tabelaPlanoAluno.setFillsViewportHeight(true);

        // Definir a largura preferida das colunas
        tabelaPlanoAluno.getColumnModel().getColumn(0).setPreferredWidth(200);  // Nome
        tabelaPlanoAluno.getColumnModel().getColumn(1).setPreferredWidth(150); // Data de Início
        tabelaPlanoAluno.getColumnModel().getColumn(2).setPreferredWidth(150); // Número do Cartão de Crédito
        tabelaPlanoAluno.getColumnModel().getColumn(3).setPreferredWidth(150); // Vencimento do Cartão de Crédito
        tabelaPlanoAluno.getColumnModel().getColumn(4).setPreferredWidth(100); // CVV do Cartão de Crédito
        
        JLabel idPlanoLabel = new JLabel("ID do Plano:");
        JTextField idPlanoTextField = new JTextField(10);
        JLabel dataInicioLabel = new JLabel("Data de Início (AAAA-MM-DD):");
        JTextField dataInicioTextField = new JTextField(10);
        JLabel numeroCartaoCreditoLabel = new JLabel("Número do Cartão de Crédito:");
        JTextField numeroCartaoCreditoTextField = new JTextField(10);
        JLabel vencimentoCartaoCreditoLabel = new JLabel("Vencimento do Cartão de Crédito (AAAA-MM-DD):");
        JTextField vencimentoCartaoCreditoTextField = new JTextField(10);
        JLabel cvvCartaoCreditoLabel = new JLabel("CVV do Cartão de Crédito:");
        JTextField cvvCartaoCreditoTextField = new JTextField(10);

        JButton confirmarButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cpf = aluno.getCpf();
                int idPlano = Integer.parseInt(idPlanoTextField.getText());
                Date dataInicio = Date.valueOf(dataInicioTextField.getText());
                String numeroCartaoCredito = numeroCartaoCreditoTextField.getText();
                Date vencimentoCartaoCredito = Date.valueOf(vencimentoCartaoCreditoTextField.getText());
                String cvvCartaoCredito = cvvCartaoCreditoTextField.getText();

                PlanoAlunoDAO planoAlunoDAO = new PlanoAlunoDAO();
                planoAlunoDAO.cadastrarPlanoAluno(cpf, idPlano, dataInicio, numeroCartaoCredito, vencimentoCartaoCredito, cvvCartaoCredito);
                // mensagem de sucesso
                InterfaceConfigurarPlano interfaceConfigurarPlano = new InterfaceConfigurarPlano();
                interfaceConfigurarPlano.criarInterface(aluno);
                frame.dispose();
                JOptionPane.showMessageDialog(null, "Plano de aluno configurado com sucesso!");
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idPlanoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(idPlanoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(dataInicioLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(dataInicioTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(numeroCartaoCreditoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(numeroCartaoCreditoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(vencimentoCartaoCreditoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(vencimentoCartaoCreditoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(cvvCartaoCreditoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(cvvCartaoCreditoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(confirmarButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(voltarButton, gbc);

        JPanel tablePanel = new JPanel();
        tablePanel.add(scrollPane);
        tablePanel.add(new JLabel("Planos do Aluno:"));
        tablePanel.add(new JScrollPane(tabelaPlanoAluno));

        // Adicionar scrollPane e panel ao frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, BorderLayout.NORTH);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(tablePanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Cliqeu em configurar
class InterfaceConfigurar {
    public void criarInterface(Aluno aluno) {
        JFrame frame = new JFrame("Configurar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JButton planoButton = new JButton("Plano");
        JButton treinoButton = new JButton("Treino");
        JButton voltarButton = new JButton("VOLTAR");

        planoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceConfigurarPlano interfaceConfigurarPlano = new InterfaceConfigurarPlano();
                interfaceConfigurarPlano.criarInterface(aluno);
                frame.dispose();
            }
        });

        treinoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceConfigurarTreino interfaceConfigurarTreino = new InterfaceConfigurarTreino(frame);
                interfaceConfigurarTreino.criarInterface(aluno);
                frame.dispose();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.getContentPane().add(planoButton);
        frame.getContentPane().add(treinoButton);
        frame.getContentPane().add(voltarButton);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Buscar por CPF
class InterfaceBuscarPorCPF {
    public void criarInterface() {
        JFrame frame = new JFrame("Buscar por CPF");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfTextField = new JTextField(10);

        JButton buscarButton = new JButton("Buscar");
        JButton voltarButton = new JButton("Voltar");

        buscarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfTextField.getText();
                AlunoDAO alunoDAO = new AlunoDAO();
                Aluno aluno = alunoDAO.buscarAlunoPorCpf(cpf);

                if (aluno != null) {
                    //mostrar informações do aluno
                    JOptionPane.showMessageDialog(null, "CPF: " + aluno.getCpf() + "\nNome: " + aluno.getNome() + "\nData de Nascimento: " + aluno.getDataNascimento());
                } else {
                    //mensagem de erro
                    JOptionPane.showMessageDialog(null, "Aluno não encontrado!");
                }
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceBuscar interfaceBuscar = new InterfaceBuscar();
                interfaceBuscar.criarInterface();
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(cpfLabel);
        panel.add(cpfTextField);
        panel.add(buscarButton);
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.getContentPane().setLayout(new FlowLayout());
    
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}

//Buscar por nome
class InterfaceBuscarPorNome {
    public void criarInterface() {

        JFrame frame = new JFrame("Buscar por Nome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeTextField = new JTextField(20);

        JButton buscarButton = new JButton("Buscar");
        JButton voltarButton = new JButton("Voltar");

        buscarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = nomeTextField.getText();
                AlunoDAO alunoDAO = new AlunoDAO();
                Aluno aluno = alunoDAO.buscarAlunoPorNome(nome);

                if (aluno != null) {
                    //mostrar informações do aluno
                    JOptionPane.showMessageDialog(null, "CPF: " + aluno.getCpf() + "\nNome: " + aluno.getNome() + "\nData de Nascimento: " + aluno.getDataNascimento());
                } else {
                    //mensagem de erro
                    JOptionPane.showMessageDialog(null, "Aluno não encontrado!");
                }
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceBuscar interfaceBuscar = new InterfaceBuscar();
                interfaceBuscar.criarInterface();
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(nomeLabel);
        panel.add(nomeTextField);
        panel.add(buscarButton);
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Cliqeu em buscar
class InterfaceBuscar {
    public void criarInterface() {
        JFrame frame = new JFrame("Buscar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JButton porNomeButton = new JButton("Por Nome");
        JButton porCPFButton = new JButton("Por CPF");
        JButton voltarButton = new JButton("Voltar");

        porNomeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceBuscarPorNome interfaceBuscarPorNome = new InterfaceBuscarPorNome();
                interfaceBuscarPorNome.criarInterface();
                frame.dispose();
            }
        });

        porCPFButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceBuscarPorCPF interfaceBuscarPorCPF = new InterfaceBuscarPorCPF();
                interfaceBuscarPorCPF.criarInterface();
                frame.dispose();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.getContentPane().add(porNomeButton);
        frame.getContentPane().add(porCPFButton);
        frame.getContentPane().add(voltarButton);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Clique em ver alunos
class InterfaceVerAlunos extends JFrame {
    private DefaultTableModel modelo;

    public void criarInterface() {
        JFrame frame = new JFrame("Ver Alunos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);

        String[] colunas = {"ID", "CPF", "Nome", "Data de Nascimento"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Criar a tabela com o modelo
        JTable tabela = new JTable(modelo);

        // Adicionar a tabela a um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabela);
        frame.add(scrollPane);

        //Criar botões de editar e excluir para cada aluno
        JButton editarButton = new JButton("Editar");
        JButton excluirButton = new JButton("Excluir");
        JButton cadastrarButton = new JButton("Cadastrar");
        JButton configurarButton = new JButton("Configurar");
        JButton buscarButton = new JButton("Buscar");
        JButton voltarButton = new JButton("VOLTAR");

        editarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    int id = (int) modelo.getValueAt(linhaSelecionada, 0);
                    String cpf = (String) modelo.getValueAt(linhaSelecionada, 1);
                    String nome = (String) modelo.getValueAt(linhaSelecionada, 2);
                    String dataNascimento = (String) modelo.getValueAt(linhaSelecionada, 3);

                    Aluno aluno = new Aluno(id, cpf, nome, dataNascimento);
                    InterfaceEditarAluno interfaceEditarAluno = new InterfaceEditarAluno(InterfaceVerAlunos.this, aluno);
                    interfaceEditarAluno.criarInterface();
                } else {
                    //mensagem de erro
                    JOptionPane.showMessageDialog(null, "Selecione um aluno para editar!");
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    String cpf = (String) modelo.getValueAt(linhaSelecionada, 1);
                    String nome = (String) modelo.getValueAt(linhaSelecionada, 2);
                    String dataNascimento = (String) modelo.getValueAt(linhaSelecionada, 3);

                    Aluno aluno = new Aluno(0, cpf, nome, dataNascimento);
                    InterfaceExcluirAluno interfaceExcluirAluno = new InterfaceExcluirAluno(InterfaceVerAlunos.this, aluno);
                    interfaceExcluirAluno.criarInterface();
                } else {
                    //mensagem de erro
                    JOptionPane.showMessageDialog(null, "Selecione um aluno para excluir!");
                }
            }
        });

        cadastrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceCadastrarAluno interfaceCadastrarAluno = new InterfaceCadastrarAluno(InterfaceVerAlunos.this);
                interfaceCadastrarAluno.criarInterface();
            }
        });

        configurarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    String cpf = (String) modelo.getValueAt(linhaSelecionada, 1);
                    String nome = (String) modelo.getValueAt(linhaSelecionada, 2);
                    String dataNascimento = (String) modelo.getValueAt(linhaSelecionada, 3);

                    Aluno aluno = new Aluno(0, cpf, nome, dataNascimento);
                    InterfaceConfigurar interfaceConfigurar = new InterfaceConfigurar();
                    interfaceConfigurar.criarInterface(aluno);
                } else {
                    //mensagem de erro
                    JOptionPane.showMessageDialog(null, "Selecione um aluno para configurar!");
                }
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceBuscar interfaceBuscar = new InterfaceBuscar();
                interfaceBuscar.criarInterface();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceInstrutor interfaceInstrutor = new InterfaceInstrutor();
                interfaceInstrutor.criarInterface();
                frame.dispose();
            }
        });

        frame.getContentPane().add(new JLabel("Alunos:"));
        frame.getContentPane().add(editarButton);
        frame.getContentPane().add(excluirButton);
        frame.getContentPane().add(cadastrarButton);
        frame.getContentPane().add(configurarButton);
        frame.getContentPane().add(buscarButton);
        frame.getContentPane().add(voltarButton);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Carregar alunos inicialmente
        carregarAlunos();
    }

    public void carregarAlunos() {
        modelo.setRowCount(0); // Limpa a tabela
        AlunoDAO alunoDAO = new AlunoDAO();
        List<Aluno> alunos = alunoDAO.listarAlunos();
        for (Aluno aluno : alunos) {
            Object[] linha = {aluno.getId(), aluno.getCpf(), aluno.getNome(), aluno.getDataNascimento()};
            modelo.addRow(linha);
        }
    }
}

//Clique em Instrutor
class InterfaceInstrutor {
    public void criarInterface() {
        JFrame frame = new JFrame("Instrutor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JButton verAlunosButton = new JButton("Ver Alunos");
        JButton cadastrarPlanoButton = new JButton("Cadastrar Plano");
        JButton cadastrarExercicioButton = new JButton("Cadastrar Exercício");
        JButton voltarButton = new JButton("Voltar");

        verAlunosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceVerAlunos interfaceVerAlunos = new InterfaceVerAlunos();
                interfaceVerAlunos.criarInterface();
                frame.dispose();
            }
        });

        cadastrarPlanoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceCadastrarPlano interfaceCadastrarPlano = new InterfaceCadastrarPlano();
                interfaceCadastrarPlano.criarInterface();
                frame.dispose();
            }
        });

        cadastrarExercicioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceCadastrarExercicio interfaceCadastrarExercicio = new InterfaceCadastrarExercicio();
                interfaceCadastrarExercicio.criarInterface();
                frame.dispose();
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceInicial interfaceInicial = new InterfaceInicial();
                interfaceInicial.criarInterface();
                frame.dispose();
            }
        });
        JPanel panel = new JPanel();
        panel.add(verAlunosButton);
        panel.add(cadastrarPlanoButton);
        panel.add(cadastrarExercicioButton);
        //botão de voltar no fim da tela
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(voltarButton);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Tela de andamento de treino de aluno
class InterfaceAndamentoTreino {
    public void criarInterface(Aluno aluno, Treino treino) {
        JFrame frame = new JFrame("Andamento de Treino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 500);

        Date dataAtual = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JLabel dataLabel = new JLabel("Treino de " + aluno.getNome() + ", dia " + sdf.format(dataAtual));

        String[] colunas = {
            "ID", "Nome", "Nº Séries", "Mín Repetições", "Máx Repetições", "Carga (kg)", "Descanso (min)",
            "Concluído", "Alteração no Peso"
        };
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7 || column == 8; // Permite edição apenas nas colunas Concluído e Alteração no Peso
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) {
                    return Boolean.class; // Coluna Concluído
                } else if (columnIndex == 8) {
                    return BigDecimal.class; // Coluna Alteração no Peso
                }
                return super.getColumnClass(columnIndex);
            }
        };

        ExercicioDAO exercicioDAO = new ExercicioDAO();

        // Adicionar os exercícios ao modelo
        ExercicioTreinoDAO exercicioTreinoDAO1 = new ExercicioTreinoDAO();
        List<ExercicioTreino> exercicios = exercicioTreinoDAO1.listarExerciciosTreino(treino.getId());
        for (ExercicioTreino exercicio : exercicios) {
            Exercicio exercicio1 = exercicioDAO.buscarExercicioPorId(exercicio.getIdExercicio());
            Object[] linha = {
                exercicio.getId(), exercicio1.getNome(), exercicio.getNumeroSeries(),
                exercicio.getRepeticoesMin(), exercicio.getRepeticoesMax(), exercicio.getCargaKg(), exercicio.getTempoDescansoMin(),
                false, (BigDecimal.ZERO)
            };
            modelo.addRow(linha);
        }

        // Criar a tabela com o modelo
        JTable tabela = new JTable(modelo);
        ajustarLarguraColunas(tabela, 120);

        // Aumentar a altura da linha em 1.5x
        tabela.setRowHeight((int) (tabela.getRowHeight() * 1.5));

        // Adicionar a tabela a um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabela);
        frame.add(dataLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Criar painel para botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton finalizarTreinoButton = new JButton("Finalizar Treino");
        JButton voltarButton = new JButton("Voltar");

        finalizarTreinoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Iterar sobre as linhas da tabela para obter os dados
                HistoricoTreinoDAO historicoTreinoDAO = new HistoricoTreinoDAO();
                int id = historicoTreinoDAO.getIdUltimoHistoricoTreino();
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    int idExercicio = (Integer) modelo.getValueAt(i, 0);
                    boolean concluido = (Boolean) modelo.getValueAt(i, 7);
                    BigDecimal alteracaoPeso = (BigDecimal) modelo.getValueAt(i, 8);

                    // Atualizar o histórico de exercícios
                    //pegar o id pk do ultimo treino no historico_treino
                    HistoricoExercicioDAO historicoExercicioDAO = new HistoricoExercicioDAO();
                    historicoExercicioDAO.cadastrarHistoricoExercicio(id, idExercicio, alteracaoPeso, concluido);
                }

                //Volta para a tela inicial de aluno
                frame.dispose();
                // Mensagem de sucesso
                JOptionPane.showMessageDialog(null, "Treino finalizado com sucesso!");

                //Editar o histórico do último treino na tabela historico_treino:
                historicoTreinoDAO.editarHistoricoTreino(historicoTreinoDAO.getIdUltimoHistoricoTreino(), dataAtual, true);
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Adicionar botões ao painel
        buttonPanel.add(finalizarTreinoButton);
        buttonPanel.add(voltarButton);

        // Adicionar painel de botões ao frame
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Centralizar o frame na tela
        frame.setLocationRelativeTo(null);
        // Exibir o frame
        frame.setVisible(true);
    }

    private void ajustarLarguraColunas(JTable tabela, int largura) {
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            TableColumn coluna = tabela.getColumnModel().getColumn(i);
            coluna.setPreferredWidth(largura);
            coluna.setMinWidth(largura);
            coluna.setMaxWidth(largura);
        }
    }
}

//Tela inicial de aluno mostrando seus treinos
class InterfaceAlunoTreinos {
    public void criarInterface(Aluno aluno) {
        JFrame frame = new JFrame("Treinos de Aluno");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        Date dataAtual = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JLabel dataLabel = new JLabel("Olá, " + aluno.getNome() + ", dia " + sdf.format(dataAtual));

        // Definir o modelo de tabela
        String[] colunas = {"ID", "Nome"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Adicionar os treinos ao modelo
        TreinoDAO treinoDAO = new TreinoDAO();
        List<Treino> treinos = treinoDAO.listarTreinos(aluno.getCpf());
        for (Treino treino : treinos) {
            Object[] linha = {treino.getId(), treino.getNome()};
            modelo.addRow(linha);
        }

        // Criar a tabela com o modelo
        JTable tabela = new JTable(modelo);

        // Adicionar a tabela a um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabela);
        frame.add(dataLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        //Criar botão de iniciar treino
        JButton iniciarTreinoButton = new JButton("Iniciar Treino");
        JButton voltarButton = new JButton("Voltar");

        iniciarTreinoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    int id = (int) modelo.getValueAt(linhaSelecionada, 0);
                    String nome = (String) modelo.getValueAt(linhaSelecionada, 1);

                    Treino treino = new Treino(aluno.getCpf(), nome, id);
                    //atualizar histórico de treinos
                    HistoricoTreinoDAO historicoTreinoDAO = new HistoricoTreinoDAO();
                    historicoTreinoDAO.cadastrarHistoricoTreino(id, dataAtual, false);
                    InterfaceAndamentoTreino interfaceAndamentoTreino = new InterfaceAndamentoTreino();
                    interfaceAndamentoTreino.criarInterface(aluno, treino);
                }else {
                    //mensagem de erro
                    JOptionPane.showMessageDialog(null, "Selecione um treino para iniciar!");
                }
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceAluno interfaceAluno = new InterfaceAluno();
                interfaceAluno.criarInterface();
                frame.dispose();
            }
        });

        frame.getContentPane().add(iniciarTreinoButton, BorderLayout.SOUTH);
        frame.getContentPane().add(voltarButton, BorderLayout.SOUTH);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Clique em Aluno
class InterfaceAluno {
    public void criarInterface() {  
        JFrame frame = new JFrame("Aluno");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfTextField = new JTextField(10);

        JButton confirmButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfTextField.getText();
                //validar cpf no banco de dados
                AlunoDAO alunoDAO = new AlunoDAO();
                Aluno aluno = alunoDAO.buscarAlunoPorCpf(cpf);
                if (aluno == null) {
                    JOptionPane.showMessageDialog(frame, "Aluno não encontrado");
                } else {
                    InterfaceAlunoTreinos interfaceAlunoTreinos = new InterfaceAlunoTreinos();
                    interfaceAlunoTreinos.criarInterface(aluno);
                    frame.dispose();
                }
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceInicial interfaceInicial = new InterfaceInicial();
                interfaceInicial.criarInterface();
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(cpfLabel);
        panel.add(cpfTextField);
        panel.add(confirmButton);
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(voltarButton);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Interface para estatísticas de um exercício em específico
class InterfaceEstatiscaExercicio {
    public void criarInterface(Aluno aluno, ExercicioTreino exercicioTreino, Treino treino) {
        // Create the main frame
        JFrame frame = new JFrame("Estatísticas de Exercício");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);

        ExercicioDAO exercicioDAO = new ExercicioDAO();
        int id = exercicioTreino.getIdExercicio();
        Exercicio exercicio = exercicioDAO.buscarExercicioPorId(id);
        String nomeExercicio = exercicio.getNome();

        JLabel exercicioLabel = new JLabel("Evolução da carga do exercício: " + nomeExercicio);

        // Definir o modelo de tabela
        String[] colunas = {"Data", "Carga (kg)"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Adicionar as estatísticas ao modelo
        HistoricoTreinoDAO historicoTreinoDAO = new HistoricoTreinoDAO();
        HistoricoExercicioDAO historicoExercicioDAO = new HistoricoExercicioDAO();
        List<HistoricoExercicio> historicoExercicios = historicoExercicioDAO.listarHistoricoExercicio(exercicioTreino.getId());
        for (HistoricoExercicio historicoExercicio : historicoExercicios) {
            int id_treino = historicoExercicio.getIdTreinoRealizado();
            Date data = historicoTreinoDAO.buscarDataTreinoPorId(id_treino);
            BigDecimal acrescimo = historicoExercicio.getAcrescimoPeso();
            Double carga_padrao = exercicioTreino.getCargaKg();
            BigDecimal carga_padrao1 = BigDecimal.valueOf(carga_padrao);
            BigDecimal carga = carga_padrao1.add(acrescimo);
            Object[] linha = {data, carga + "kg"};
            modelo.addRow(linha);
        }

        // Criar a tabela
        JTable tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);

        ajustarLarguraColunas(tabela, 150);

        // Criar botão
        JButton voltarButton = new JButton("VOLTAR");

        // Adicionar ação ao botão
        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Adicionar componentes ao painel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(voltarButton);

        // Adicionar scrollPane e buttonPanel ao frame
        frame.getContentPane().add(exercicioLabel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Centralizar o frame na tela
        frame.setLocationRelativeTo(null);
        // Exibir o frame
        frame.setVisible(true);
    }

    private void ajustarLarguraColunas(JTable tabela, int largura) {
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            TableColumn coluna = tabela.getColumnModel().getColumn(i);
            coluna.setPreferredWidth(largura);
            coluna.setMinWidth(largura);
            coluna.setMaxWidth(largura);
        }
    }
}

//Interface para selecionar exercício
class InterfaceRelatorioExerciciosTreino {
    public void criarInterface(Aluno aluno, Treino treino) {
        JFrame frame = new JFrame("Selecionar Exercício de Treino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1060, 600);

        String[] colunas = {
            "ID", "Nome do Treino", "Nome do Exercício", "Número de Séries",
            "Repetições Mínimas", "Repetições Máximas", "Carga (kg)", "Tempo de Descanso (min)"
        };
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Preencher a tabela com os exercícios do treino
        ExercicioTreinoDAO exercicioTreinoDAO = new ExercicioTreinoDAO();
        ExercicioDAO exercicioDAO = new ExercicioDAO();
        TreinoDAO treinoDAO = new TreinoDAO();
        List<ExercicioTreino> exerciciosTreino = exercicioTreinoDAO.listarExerciciosTreino(treino.getId());
        for (ExercicioTreino exercicioTreino : exerciciosTreino) {
            int idExercicio = exercicioTreino.getIdExercicio();
            int idTreino = exercicioTreino.getIdTreino();
            String nomeTreino = treinoDAO.buscarNomeTreinoPorId(idTreino);
            Exercicio exercicio = exercicioDAO.buscarExercicioPorId(idExercicio);
            Object[] linha = {
                exercicioTreino.getId(), nomeTreino, exercicio.getNome(),
                exercicioTreino.getNumeroSeries(), exercicioTreino.getRepeticoesMin(), exercicioTreino.getRepeticoesMax(),
                exercicioTreino.getCargaKg(), exercicioTreino.getTempoDescansoMin()
            };
            modelo.addRow(linha);
        }

        // Criar a tabela
        JTable tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);

        // Ajustar a largura das colunas para 30 pixels
        ajustarLarguraColunas(tabela, 130);

        // Criar botões
        JButton estatisticaButton = new JButton("Ver estatísticas");
        JButton voltarButton = new JButton("VOLTAR");

        // Adicionar ação aos botões, ao clicar em um exercício, vai para a tela de informações do exercício
        estatisticaButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int linhaSelecionada = tabela.getSelectedRow();
                    if (linhaSelecionada != -1) {
                        ExercicioTreino exercicioTreino = exerciciosTreino.get(linhaSelecionada);
                        InterfaceEstatiscaExercicio interfaceEstatiscaExercicio = new InterfaceEstatiscaExercicio();
                        interfaceEstatiscaExercicio.criarInterface(aluno, exercicioTreino, treino);
                    } else {
                        //mensagem de erro
                        JOptionPane.showMessageDialog(null, "Selecione um exercício para ver as estatísticas!");
                    }
                }
            });
        
        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceRelatorioExercicios interfaceRelatorioExercicios = new InterfaceRelatorioExercicios();
                interfaceRelatorioExercicios.criarInterface(aluno);
                frame.dispose();
            }
        });

        // Adicionar componentes ao painel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(estatisticaButton);
        buttonPanel.add(voltarButton);

        // Adicionar scrollPane e buttonPanel ao frame
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Centralizar o frame na tela
        frame.setLocationRelativeTo(null);
        // Exibir o frame
        frame.setVisible(true);
    }

    private void ajustarLarguraColunas(JTable tabela, int largura) {
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            TableColumn coluna = tabela.getColumnModel().getColumn(i);
            coluna.setPreferredWidth(largura);
            coluna.setMinWidth(largura);
            coluna.setMaxWidth(largura);
        }
    }
}

//Interface para relatório de exercícios
class InterfaceRelatorioExercicios {
    public void criarInterface(Aluno aluno) {
        JFrame frame = new JFrame("Relatório de Exercícios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        JLabel treinoLabel = new JLabel("Treino:");

        // Definir o modelo de tabela
        String[] colunas = {"ID", "Nome"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Adicionar os treinos ao modelo
        TreinoDAO treinoDAO = new TreinoDAO();
        List<Treino> treinos = treinoDAO.listarTreinos(aluno.getCpf());
        for (Treino treino : treinos) {
            Object[] linha = {treino.getId(), treino.getNome()};
            modelo.addRow(linha);
        }

        // Criar a tabela com o modelo
        JTable tabela = new JTable(modelo);

        // Adicionar a tabela a um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabela);
        frame.add(treinoLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Criar painel para botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton verExerciciosButton = new JButton("Ver Exercícios");
        JButton voltarButton = new JButton("Voltar");

        verExerciciosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    int id = (int) modelo.getValueAt(linhaSelecionada, 0);
                    String nome = (String) modelo.getValueAt(linhaSelecionada, 1);

                    Treino treino = new Treino(aluno.getCpf(), nome, id);
                    InterfaceRelatorioExerciciosTreino interfaceRelatorioExerciciosTreino = new InterfaceRelatorioExerciciosTreino();
                    interfaceRelatorioExerciciosTreino.criarInterface(aluno, treino);
                    frame.dispose();
                }else {
                //mensagem de erro
                JOptionPane.showMessageDialog(null, "Selecione um treino para ver os exercícios!");
            }
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceRelatorioAluno interfaceRelatorioAluno = new InterfaceRelatorioAluno();
                interfaceRelatorioAluno.criarInterface(aluno);
                frame.dispose();
            }
        });

        frame.getContentPane().add(verExerciciosButton, BorderLayout.SOUTH);
        frame.getContentPane().add(voltarButton, BorderLayout.SOUTH);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Interface para relatório de aluno
class InterfaceRelatorioAluno {
    public void criarInterface(Aluno aluno) {
        JFrame frame = new JFrame("Relatório de Aluno");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);

        JLabel presencaLabel = new JLabel("Presença nos últimos 30 dias");

        // Definir o modelo de tabela
        String[] colunasPresenca = {"Data", "Treino", "Concluído", "Exercícios Concluídos"};
        DefaultTableModel modeloPresenca = new DefaultTableModel(colunasPresenca, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // torna as células da tabela não editáveis
            }
        };

        // Consultar pegando o id do treino qual é o seu nome
        HistoricoTreinoDAO historicoTreinoDAO = new HistoricoTreinoDAO();
        List<HistoricoTreino> historicoTreinos = historicoTreinoDAO.listarHistoricoTreinos(aluno.getCpf());
        for (HistoricoTreino historicoTreino : historicoTreinos) {
            TreinoDAO treinoDAO = new TreinoDAO();
            String nome_treino = treinoDAO.buscarNomeTreinoPorId(historicoTreino.getIdTreino());
            HistoricoExercicioDAO historicoExercicioDAO = new HistoricoExercicioDAO();
            List<HistoricoExercicio> historicoExercicios = historicoExercicioDAO.listarHistoricoExercicios(historicoTreino.getId());
            // Juntar os nomes dos exercícios em uma string
            StringBuilder exerciciosConcluidos = new StringBuilder();
            for (HistoricoExercicio historicoExercicio : historicoExercicios) {
                ExercicioDAO exercicioDAO = new ExercicioDAO();
                ExercicioTreinoDAO ExercicioTreinoDAO = new ExercicioTreinoDAO();
                //procurar o id pk do exercicio na tabela treino_exercicios e pegar o id do exercicio
                int id_pk = historicoExercicio.getIdExercicio();
                int id_exercicio = ExercicioTreinoDAO.buscarIdExercicioPorIdPK(id_pk);
                Exercicio exercicio = exercicioDAO.buscarExercicioPorId(id_exercicio);
                exerciciosConcluidos.append(exercicio.getNome()).append(", ");
            }
            Object[] linha = {historicoTreino.getDataTreino(), nome_treino, historicoTreino.isTreinoConcluido(), exerciciosConcluidos};
            modeloPresenca.addRow(linha);
        }

        // Criar a tabela com o modelo
        JTable tabelaPresenca = new JTable(modeloPresenca);

        // Ajustar larguras das colunas
        ajustarLarguraColunas(tabelaPresenca, 100, 400);

        // Adicionar a tabela a um JScrollPane
        JScrollPane scrollPanePresenca = new JScrollPane(tabelaPresenca);

        // Criar painel para botão de voltar
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton voltarButton = new JButton("Voltar");

        // Add action listener to button
        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceRelatorio interfaceRelatorio = new InterfaceRelatorio();
                interfaceRelatorio.criarInterface();
                frame.dispose();
            }
        });

        // Adicionar componentes ao frame
        frame.add(presencaLabel, BorderLayout.NORTH);
        frame.add(scrollPanePresenca, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Adicionar botão de voltar ao painel
        buttonPanel.add(voltarButton);
        // Botão para ir para a tela de relatório de exercícios
        JButton relatorioExerciciosButton = new JButton("Relatório de Exercícios");

        relatorioExerciciosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceRelatorioExercicios interfaceRelatorioExercicios = new InterfaceRelatorioExercicios();
                interfaceRelatorioExercicios.criarInterface(aluno);
                frame.dispose();
            }
        });

        // Adicionar botão ao painel
        buttonPanel.add(relatorioExerciciosButton);
        // Centralizar o frame na tela
        frame.setLocationRelativeTo(null);
        // Exibir o frame
        frame.setVisible(true);
    }

    private void ajustarLarguraColunas(JTable tabela, int larguraPadrao, int larguraExerciciosConcluidos) {
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            TableColumn coluna = tabela.getColumnModel().getColumn(i);
            if (i == 3) {
                coluna.setPreferredWidth(larguraExerciciosConcluidos);
                coluna.setMinWidth(larguraExerciciosConcluidos);
                coluna.setMaxWidth(larguraExerciciosConcluidos);
            } else {
                coluna.setPreferredWidth(larguraPadrao);
                coluna.setMinWidth(larguraPadrao);
                coluna.setMaxWidth(larguraPadrao);
            }
        }
    }
}

//Clique em Relatório
class InterfaceRelatorio {
    public void criarInterface() {
        JFrame frame = new JFrame("Relatório");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 200);

        JLabel cpfLabel = new JLabel("CPF:");
        JTextField cpfTextField = new JTextField(10);

        JButton confirmButton = new JButton("Confirmar");
        JButton voltarButton = new JButton("Voltar");

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfTextField.getText();
                //validar cpf no banco de dados
                AlunoDAO alunoDAO = new AlunoDAO();
                Aluno aluno = alunoDAO.buscarAlunoPorCpf(cpf);
                if (aluno == null) {
                    JOptionPane.showMessageDialog(frame, "Aluno não encontrado");
                } else {
                    InterfaceRelatorioAluno interfaceRelatorioAluno = new InterfaceRelatorioAluno();
                    interfaceRelatorioAluno.criarInterface(aluno);
                    frame.dispose();
                }
            }
        });

        voltarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceInicial interfaceInicial = new InterfaceInicial();
                interfaceInicial.criarInterface();
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.add(cpfLabel);
        panel.add(cpfTextField);
        panel.add(confirmButton);
        panel.add(voltarButton);

        frame.getContentPane().add(panel);

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

//Interface inicial
class InterfaceInicial {
    public void criarInterface() {
        JFrame frame = new JFrame("Programa de Treinos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JButton alunoButton = new JButton("Aluno");
        JButton instrutorButton = new JButton("Instrutor");
        JButton relatorioButton = new JButton("Relatório");

        alunoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceAluno interfaceAluno = new InterfaceAluno();
                interfaceAluno.criarInterface();
                frame.dispose();
            }
        });

        instrutorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceInstrutor interfaceInstrutor = new InterfaceInstrutor();
                interfaceInstrutor.criarInterface();
                frame.dispose();
            }
        });

        relatorioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceRelatorio interfaceRelatorio = new InterfaceRelatorio();
                interfaceRelatorio.criarInterface();
                frame.dispose();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center alignment with horizontal gap

        topPanel.add(alunoButton);
        topPanel.add(instrutorButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(topPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        JPanel relatorioPanel = new JPanel();
        relatorioPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        relatorioPanel.add(relatorioButton);
        mainPanel.add(relatorioPanel);

        mainPanel.add(Box.createVerticalGlue());

        frame.getContentPane().add(mainPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// Classe Principal para Testar as Operações
public class T1 {
    public static void main(String[] args) {
        InterfaceInicial interfaceInicial = new InterfaceInicial();
        interfaceInicial.criarInterface();
    }
}