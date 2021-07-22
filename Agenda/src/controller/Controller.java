package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

// TODO: Auto-generated Javadoc
/**
 * The Class Controller.
 */
// recebendo os dados do form
@WebServlet(urlPatterns = {"/Controller","/main","/insert","/update","/save","/delete","/report"}) //caminhos das requisições recebidas pelas urls
public class Controller extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The dao. */
	DAO dao = new DAO();
	
	/** The contato. */
	JavaBeans contato = new JavaBeans();
	
    /**
     * Instantiates a new controller.
     */
    public Controller() {
        super();        
    }

	/**
	 * Do get.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//teste conexão comentado na classe DAO.java
		//dao.testeConexao();
		
		String action = request.getServletPath();
		
		if (action.equals("/main")) {
			contatos(request,response);
	
		} else if (action.equals("/insert")) {
			adicionarContato(request,response);
			
		} else if (action.equals("/update")) {
			editarContato(request,response);
			
		} else if (action.equals("/save")) {
			salvarContato(request,response);
			
		} else if (action.equals("/delete")) {
			removerContato(request,response);
			
		} else if (action.equals("/report")) {
			geraRelatorio(request,response);
			
		} else {
			response.sendRedirect("index.html");
		}
		
	}
	
	/**
	 * Contatos.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// Listar contatos
	protected void contatos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<JavaBeans> lista = dao.listarContatos();
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);
	}
	
	/**
	 * Adicionar contato.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// Novo contato
	protected void adicionarContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		contato.setNome(request.getParameter("nome"));
		contato.setTelefone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		dao.criarContato(contato); // passando o objeto para o banco
		
		response.sendRedirect("main");
	}	
	
	/**
	 * Editar contato.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void editarContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		contato.setId(Integer.parseInt(request.getParameter("id")));
		dao.listaContato(contato);
		request.setAttribute("id",contato.getId());
		request.setAttribute("nome",contato.getNome());
		request.setAttribute("fone",contato.getTelefone());
		request.setAttribute("email",contato.getEmail());
		
		RequestDispatcher rd = request.getRequestDispatcher("editar.jsp");
		rd.forward(request, response);
	}
	
	/**
	 * Salvar contato.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void salvarContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		contato.setId(Integer.parseInt((request.getParameter("id"))));
		contato.setNome(request.getParameter("nome"));
		contato.setTelefone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		dao.alterarContato(contato);
		response.sendRedirect("main");
		
	}
	
	/**
	 * Remover contato.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void removerContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		contato.setId(Integer.parseInt(request.getParameter("id")));
		dao.deletarContato(contato);
		response.sendRedirect("main");
	}
	
	/**
	 * Gera relatorio.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void geraRelatorio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Document documento = new Document();
		
		try {
			//tipo do documento
			response.setContentType("apllication/pdf");
			//nome do documento
			response.addHeader("Content-Disposition","inline; filename=" + "contatos.pdf");
			// criar documento
			PdfWriter.getInstance(documento, response.getOutputStream());
			//abrir documento -> gera conteudo
			documento.open();
			documento.add(new Paragraph ("Lista de Contatos:"));
			documento.add(new Paragraph (" "));
			// criar uma tabela
			PdfPTable tabela =  new PdfPTable(3);
			// cria cabeçalho
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Telefone"));
			PdfPCell col3 = new PdfPCell(new Paragraph("E-mail"));
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			// popular tabela com os contatos
			ArrayList<JavaBeans> lista = dao.listarContatos();
			
			for (int i = 0; i < lista.size(); i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getTelefone());
				tabela.addCell(lista.get(i).getEmail());
			}
			
			documento.add(tabela);
			documento.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			documento.close();
		}
		
	}

}
