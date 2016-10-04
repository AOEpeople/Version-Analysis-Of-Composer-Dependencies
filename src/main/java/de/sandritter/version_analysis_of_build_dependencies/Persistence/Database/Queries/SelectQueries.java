package de.sandritter.version_analysis_of_build_dependencies.Persistence.Database.Queries;

/**
 * SelectQueries.java This object is used as helper to provide all select
 * queries of the database
 *
 * @author Michael Sandritter
 */
public class SelectQueries {

	/**
	 * select query for loading all dependencies of a specific build by it's
	 * buildId
	 */
	public String highLevelDependencies = "SELECT z.component_name as component, z.reference "
			+ "AS reference, z.version AS version, z.build_id AS build_id, "
			+ "z.type AS type, x.source_url AS source_url, x.source_type AS source_type "
			+ "FROM component AS x INNER JOIN "
			+ "(SELECT d.reference, s.version, s.component_name, s.reference, d.build_id, d.type FROM stand AS s INNER JOIN "
			+ "(SELECT reference, build_id, type FROM dependency WHERE build_id=? AND type='direct') AS d "
			+ "ON s.reference = d.reference) as z " + "ON z.component_name = x.name";

	/**
	 * select query for loading all dependencies (including the main component)
	 * of a specific build by it's buildId
	 */
	public String allDependencies = "SELECT ds.component_name AS component, ds.reference AS reference, "
			+ "ds.version AS version, ds.build_id AS build_id, ds.type AS type, "
			+ "c.source_url as source_url, c.source_type AS source_type FROM component AS c INNER JOIN "
			+ "(SELECT * FROM stand AS s INNER JOIN " + "(SELECT * FROM dependency WHERE build_id=?) AS d "
			+ "ON s.reference = d.reference) AS ds " + "ON ds.component_name = c.name";

	/**
	 * select query for loading the main component of a specific build by it's
	 * buildId
	 */
	public String mainComponent = "SELECT ds.component_name AS component, ds.reference AS reference, "
			+ "ds.version AS version, ds.build_id AS build_id, ds.type AS type, "
			+ "c.source_url as source_url, c.source_type AS source_type FROM component AS c INNER JOIN "
			+ "(SELECT d.reference, s.version, s.component_name, s.reference, d.build_id, d.type FROM stand AS s INNER JOIN "
			+ "(SELECT * FROM dependency WHERE build_id=? AND type='main') AS d "
			+ "ON d.reference = s.reference) AS ds " + "ON ds.component_name = c.name";

	/**
	 * select query for loading build-specific information of a build by the
	 * reference of it's main component
	 */
	public String buildOfMainDependency = "SELECT  build_id, time_stamp, build_number, job_name, job_url FROM build AS b "
			+ "INNER JOIN (" + "SELECT build_id FROM dependency WHERE reference=? AND type='main') AS d "
			+ "ON b.id = d.build_id";

	/**
	 * select query for loading all components which are dependent on a
	 * component by given componentName
	 */
	public String dependentComponents = "SELECT stand.component_name component_name, stand.reference reference, stand.version version, F.build_id build_id, F.job_name job_name, F.maxTime time_stamp, F.build_number build_number, F.job_url job_url FROM stand INNER JOIN (SELECT * FROM dependency INNER JOIN (SELECT MAX(time_stamp) maxTime, build.job_name job_name, build.id build_id, build.build_number build_number, build.job_url job_url FROM build INNER JOIN (SELECT dependency.build_id FROM dependency NATURAL JOIN stand WHERE stand.component_name=? AND dependency.type='direct') AS D ON build.id=D.build_id GROUP BY build.job_name ) AS E ON E.build_id=dependency.build_id WHERE dependency.type='main') AS F ON stand.reference=F.reference";
}
