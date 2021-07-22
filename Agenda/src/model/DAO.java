package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class DAO.
 */
public class DAO {	
	// Modulo de Conexão	
	/** The driver. */
	//Parametros de conexão
	private String driver = "com.mysql.cj.jdbc.Driver";
	
	/** The url. */
	private String url = "jdbc:mysql://127.0.0.1:3306/db_agenda?useTimeZone=true&serverTimezone=UTC";
	
	/** The user. */
	private String user = "root";
	
	/** The password. */
	private String password = "root";
	
	/**
	 * Conectar.
	 *
	 * @return the connection
	 */
	//Metodo de conexão
	private  Connection conectar() {
		Connection con = null;
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,user,password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	/**
	 * Criar contato.
	 *
	 * @param contato the contato
	 */
	public void criarContato(JavaBeans contato) {
		
		String query = "INSERT INTO contatos (nome,telefone,email) VALUES (?,?,?)";
		
		try {
			// abrir conexão
			Connection con = conectar();
			// preparar query para execução no banco de dados
			PreparedStatement pst = con.prepareStatement(query);
			// substituir os parametros
			pst.setString(1, contato.getNome()); // 1 se refere a 1° ?
			pst.setString(2, contato.getTelefone()); //2 se refere a 2° ?
			pst.setString(3, contato.getEmail());// 3 se refere a 3° ?
			
			pst.executeUpdate(); // executa a query
			con.close(); //fecha a conexao
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Listar contatos.
	 *
	 * @return the array list
	 */
	public ArrayList<JavaBeans> listarContatos(){
		ArrayList<JavaBeans> contatos = new ArrayList<>();
		String query = "SELECT * FROM contatos ORDER BY nome";
		 
		try {
			
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet result = pst.executeQuery(); // resultado é armazenado temporariamente
			
			while (result.next()) {
				//variaveis temporarias 
				int  id = Integer.parseInt(result.getString(1));
				String nome =  result.getString(2);
				String telefone =  result.getString(3);
				String email =  result.getString(4);
				// adicionando os dados 
				contatos.add(new JavaBeans(id,nome,telefone,email));
			}
			con.close();
			return contatos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

/**
 * Lista contato.
 *
 * @param contato the contato
 */
public void listaContato(JavaBeans contato) {
	String query = "SELECT * FROM contatos WHERE id = ?";
	 
	try {
		
		Connection con = conectar();
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1,contato.getId());
		
		ResultSet result = pst.executeQuery(); // resultado é armazenado temporariamente
		
		while (result.next()) {
			//variaveis temporarias 
			contato.setId(Integer.parseInt(result.getString(1)));
			contato.setNome(result.getString(2)); // numeros correspondem às colunas da tabela no banco
			contato.setTelefone(result.getString(3));
			contato.setEmail(result.getString(4));
			
		}
		con.close();
	} catch (Exception e) {
		e.printStackTrace();
		
	}
}

/**
 * Alterar contato.
 *
 * @param contato the contato
 */
public void alterarContato(JavaBeans contato) {
	String query = "UPDATE contatos SET nome = ?, telefone = ?, email = ? WHERE id = ?";

	try {
		
		Connection con = conectar();
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,contato.getNome());
		pst.setString(2,contato.getTelefone());
		pst.setString(3,contato.getEmail());
		pst.setInt(4,contato.getId());
		pst.executeUpdate();
		con.close();
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}

/**
 * Deletar contato.
 *
 * @param contato the contato
 */
public void deletarContato (JavaBeans contato) {
	String delete = "DELETE FROM contatos WHERE id = ?";
	try {
		
		Connection con = conectar();
		PreparedStatement pst = con.prepareStatement(delete);
		pst.setInt(1, contato.getId());
		pst.executeUpdate();
		con.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	/*/teste conexao
	public void testeConexao() {
		try {
			Connection con = conectar();
			System.out.println(con);
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}*/
}

