package de.sandritter.version_analysis_of_build_dependencies.Persistence.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Mapping.Interface.DatabaseMapper;
import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Persistence.Database.Interface.DataLoader;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.BuildSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.ComponentSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.DependentComponent;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Mapping.Enum.DependencyType;
import de.sandritter.version_analysis_of_build_dependencies.Persistence.Database.Queries.SelectQueries;

/**
 * DBLoader.java 
 * This class handles all requests that are send to database to select data
 *
 * @author Michael Sandritter
 */
public class SQliteLoader implements DataLoader {

	/**
	 * path to the database file
	 */
	private String dbPath;

	/**
	 * mapper that maps the ResultSet to model objects {@link DatabaseMapper}
	 */
	private DatabaseMapper mapper;
	
	/**
	 * class that holds queries for fetching data from database
	 */
	private SelectQueries queries;

	/**
	 * @param dbPath database path
	 * @param mapper {@link DatabaseMapper}
	 */
	@Inject
	public SQliteLoader(@Assisted String dbPath, DatabaseMapper mapper)
	{
		this.dbPath = dbPath;
		this.mapper = mapper;
		this.queries = new SelectQueries();
	}

	/**
	 * is establishing a connection with the databse
	 * 
	 * @return {@link Connection}
	 * @throws Exception
	 */
	private Connection establishConnection() throws Exception
	{
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			c.setAutoCommit(false);
		} catch (Exception e) {
			throw new Exception("[DBLoader.java cannot establish connection]: " + dbPath + "\n" + e.getMessage(), e);
		}

		return c;
	}

	@Override
	public Transferable loadDependencies(String value, DependencyType type) throws Exception
	{
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = establishConnection();
			String sql = "";
			if (type == DependencyType.HIGH_LEVEL) {
				sql = queries.highLevelDependencies;
			} else if (type == DependencyType.ALL) {
				sql = queries.allDependencies;
			}
			stmt = c.prepareStatement(sql);
			stmt.setString(1, value);

			ResultSet rs = stmt.executeQuery();
			return mapper.mapComponentResults(rs);
		} catch (SQLException e) {
			throw new SQLException("[DBLoader.java cannot dependencies]: " + e.getMessage(), e);
		} finally {
			closeStatementAndConnection(stmt, c);
		}
	}

	@Override
	public Transferable loadBuild(String reference) throws Exception
	{
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = establishConnection();
			String sql = queries.buildOfMainDependency;
			stmt = c.prepareStatement(sql);
			stmt.setString(1, reference);

			ResultSet rs = stmt.executeQuery();
			return mapper.mapResult(rs, BuildSummary.class);
		} catch (SQLException e) {
			throw new SQLException("[DBLoader.java cannot load build]: " + e.getMessage(), e);
		} finally {
			closeStatementAndConnection(stmt, c);
		}
	}

	@Override
	public Transferable loadMainComponent(String buildId) throws Exception
	{
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = establishConnection();
			String sql = queries.mainComponent;
			stmt = c.prepareStatement(sql);
			stmt.setString(1, buildId);
			ResultSet rs = stmt.executeQuery();
			return mapper.mapResult(rs, ComponentSummary.class);
		} catch (SQLException e) {
			throw new SQLException("[DBLoader.java cannot load main component]: " + e.getMessage(), e);
		} finally {
			closeStatementAndConnection(stmt, c);
		}
	}

	public Transferable loadDependentComponents(String dependencyName) throws Exception
	{
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = establishConnection();
			String sql = queries.dependentComponents;
			stmt = c.prepareStatement(sql);
			stmt.setString(1, dependencyName);

			ResultSet rs = stmt.executeQuery();
			return mapper.mapResult(rs, DependentComponent.class);
		} catch (SQLException e) {
			throw new SQLException("[DBLoader.java cannot load main component]: " + e.getMessage(), e);
		} finally {
			closeStatementAndConnection(stmt, c);
		}
	}

	private void closeStatementAndConnection(PreparedStatement stmt, Connection c) throws SQLException
	{
		if (stmt != null)
			stmt.close();
		if (c != null)
			c.close();
	}
}
