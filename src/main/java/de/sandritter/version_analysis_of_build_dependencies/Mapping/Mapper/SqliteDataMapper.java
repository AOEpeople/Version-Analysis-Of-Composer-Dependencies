package de.sandritter.version_analysis_of_build_dependencies.Mapping.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.sandritter.version_analysis_of_build_dependencies.DependencyConfiguration.Mapping.Interface.DatabaseMapper;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.BuildSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.ComponentSummary;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Result.Database.DependentComponent;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Transport;
import de.sandritter.version_analysis_of_build_dependencies.Domain.Model.Transfer.Interface.Transferable;
import de.sandritter.version_analysis_of_build_dependencies.Persistence.Database.Enum.Field;

/**
 * SqliteDataMapper.java 
 * This object maps {@link ResultSet} from the database to model objects
 *
 * @author Michael Sandritter
 */
public class SqliteDataMapper implements DatabaseMapper {

	@Override
	public Transferable mapComponentResults(ResultSet result) throws SQLException
	{
		Transferable transport = new Transport();
		Map<String, ComponentSummary> map = saveComponentSummary(result);
		transport.setMap(ComponentSummary.class, map);
		return transport;
	}

	@Override
	public Transferable mapResult(ResultSet result, Class<?> cls) throws SQLException
	{
		Transferable transport = new Transport();
		BuildSummary buildSum = null;
		if (cls == BuildSummary.class) {
			buildSum = saveBuildSummary(result);
			transport.setObject(BuildSummary.class, buildSum);
		} else if (cls == ComponentSummary.class) {
			Map<String, ComponentSummary> map = saveComponentSummary(result);
			if (map.size() == 1) {
				String key = (String) map.keySet().toArray()[0];
				transport.setObject(ComponentSummary.class, map.get(key));
			}
		} else if (cls == DependentComponent.class) {
			transport.setMap(cls, saveDependentComponent(result));
		}
		return transport;
	}

	/**
	 * is extracting build-specific information from the database ResultSet.
	 * saves extracted data in {@link BuildSummary}
	 * 
	 * @param result {@link ResultSet}
	 * @return {@link BuildSummary}
	 * @throws SQLException
	 */
	private BuildSummary saveBuildSummary(ResultSet result) throws SQLException
	{

		BuildSummary buildSum = null;
		while (result.next()) {
			buildSum = new BuildSummary();
			buildSum.setBuildId(result.getString(Field.BUILD_ID.toString()));
			buildSum.setTime_stamp(result.getLong(Field.TIMESTAMP.toString()));
			buildSum.setJobName(result.getString(Field.JOB_NAME.toString()));
			buildSum.setBuildNumber(result.getInt(Field.BUILD_NUMBER.toString()));
			buildSum.setJobUrl(result.getString(Field.JOB_URL.toString()));
		}
		return buildSum;
	}

	/**
	 * is extracting component- and version-specific information from the
	 * database ResultSet and saves extracted data in  Map<String,{@linkComponentSummary}>
	 * 
	 * @param result {@link ResultSet}
	 * @return Map<String, {@link ComponentSummary}>
	 * @throws SQLException
	 */
	private Map<String, ComponentSummary> saveComponentSummary(ResultSet result) throws SQLException
	{
		Map<String, ComponentSummary> map = new HashMap<String, ComponentSummary>();
		while (result.next()) {
			ComponentSummary sum = new ComponentSummary();
			String reference = result.getString(Field.REFERENCE.toString());
			sum.setBuildId(result.getString(Field.BUILD_ID.toString()));
			sum.setReference(reference);
			sum.setDependencyType(result.getString(Field.DEP_TYPE.toString()));
			sum.setVersion(result.getString(Field.VERSION.toString()));
			sum.setComponentName(result.getString(Field.COMPONENT.toString()));
			sum.setSourceUrl(result.getString(Field.VC_URL.toString()));
			sum.setSourceType(result.getString(Field.VC_TYPE.toString()));
			map.put(reference, sum);
		}
		return map;
	}

	/**
	 * extracts highlevel components of lowlevel dependency as
	 * database ResultSet and saves extracted data in Map<String,{@linkComponentSummary}>
	 * 
	 * @param result
	 * @return Map<String, {@link DependentComponent}>
	 * @throws SQLException
	 */
	private Map<String, DependentComponent> saveDependentComponent(ResultSet result) throws SQLException
	{

		Map<String, DependentComponent> map = new HashMap<String, DependentComponent>();
		while (result.next()) {
			DependentComponent c = new DependentComponent();
			c.setComponentName(result.getString(Field.COMPONENT_NAME.toString()));
			c.setJobName(result.getString(Field.JOB_NAME.toString()));
			c.setReference(result.getString(Field.REFERENCE.toString()));
			c.setVersion(result.getString(Field.VERSION.toString()));
			c.setBuildId(result.getString(Field.BUILD_ID.toString()));
			c.setBuildNumber(result.getInt(Field.BUILD_NUMBER.toString()));
			c.setTimeStamp(result.getLong(Field.TIMESTAMP.toString()));
			c.setLink(result.getString(Field.JOB_URL.toString()));
			if (!map.containsKey(c.getComponentName())) {
				map.put(c.getComponentName(), c);
			}
		}
		return map;
	}
}
