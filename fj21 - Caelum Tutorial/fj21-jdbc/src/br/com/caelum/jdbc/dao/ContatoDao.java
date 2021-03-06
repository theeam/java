package br.com.caelum.jdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.caelum.jdbc.ConnectionFactory;
import br.com.caelum.jdbc.modelo.Contato;

public class ContatoDao {
	private Connection connection;

	public ContatoDao() {
		this.connection = new ConnectionFactory().getConnection();
	}

	public void adiciona(Contato contato) {
		String sql = "insert into contatos " + "(nome,email,endereco,dataNascimento)" + " values (?,?,?,?)";

		try {
			// prepared statement para insercao
			PreparedStatement stmt = connection.prepareStatement(sql);

			// seta os valores
			stmt.setString(1, contato.getNome());
			stmt.setString(2, contato.getEmail());
			stmt.setString(3, contato.getEndereco());
			stmt.setDate(4, new Date(contato.getDataNascimento().getTimeInMillis()));

			// executa
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Contato> getLista() {
		try {
			List<Contato> contatos = new ArrayList<Contato>();
			PreparedStatement stmt = this.connection.prepareStatement("select * from contatos");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				// criando o objeto contato
				Contato contato = new Contato();
				contato.setId(rs.getLong("id"));
				contato.setNome(rs.getString("nome"));
				contato.setEmail(rs.getString("email"));
				contato.setEndereco(rs.getString("endereco"));

				// montando a data atraves do calendar
				Calendar data = Calendar.getInstance();
				data.setTime(rs.getDate("dataNascimento"));
				contato.setDataNascimento(data);

				// adicionando o objeto a lista
				contatos.add(contato);
				if (contatos.isEmpty()) {
					System.out.println("Nenhum Contato Cadastrado!");
				}
			}
			rs.close();
			stmt.close();
			return contatos;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Contato pesquisar(int id) {
		try {

			PreparedStatement stmt = this.connection.prepareStatement("select * from contatos where id=" + id);
			ResultSet rs = stmt.executeQuery();
			Contato contato = null;

			while (rs.next()) {
				contato = new Contato();
				contato.setId(rs.getLong("id"));
				contato.setNome(rs.getString("nome"));
				contato.setEmail(rs.getString("email"));
				contato.setEndereco(rs.getString("endereco"));

				// montando a data atraves do calendar
				Calendar data = Calendar.getInstance();
				data.setTime(rs.getDate("dataNascimento"));
				contato.setDataNascimento(data);

			}

			rs.close();
			stmt.close();

			return contato;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean altera(Contato contato) {
		Boolean alteracao = false;
		String sql = "update contatos set nome=?, email=?, endereco=?, dataNascimento=? where id=?";

		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, contato.getNome());
			stmt.setString(2, contato.getEmail());
			stmt.setString(3, contato.getEndereco());
			stmt.setDate(4, new Date(contato.getDataNascimento().getTimeInMillis()));
			stmt.setLong(5, contato.getId());

			alteracao = true;

			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return alteracao;
	}

	public boolean remove(Contato contato) {
		boolean removido = false;
		try {
			PreparedStatement stmt = connection.prepareStatement("delete from contatos where id=?");
			stmt.setLong(1, contato.getId());
			stmt.execute();
			stmt.close();
			removido = true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return removido;
	}

}
