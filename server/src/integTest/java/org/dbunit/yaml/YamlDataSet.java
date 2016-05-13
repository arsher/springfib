package org.dbunit.yaml;

import com.google.common.collect.Maps;
import org.dbunit.dataset.*;
import org.dbunit.dataset.datatype.DataType;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlDataSet implements IDataSet {
    private final Map<String, MyTable> tables = Maps.newLinkedHashMap();

    public YamlDataSet(final InputStream inputStream) throws FileNotFoundException {
        @SuppressWarnings("unchecked") final
        Map<String, List<Map<String, Object>>> data = (Map<String, List<Map<String, Object>>>) new Yaml().load(inputStream);
        for (final Map.Entry<String, List<Map<String, Object>>> ent : data.entrySet()) {
            final String tableName = ent.getKey();
            final List<Map<String, Object>> rows = ent.getValue();
            createTable(tableName, rows);
        }
    }

    class MyTable implements ITable {
        String name;
        List<Map<String, Object>> data;
        ITableMetaData meta;

        MyTable(final String name, final List<String> columnNames) {
            this.name = name;
            this.data = new ArrayList<>();
            meta = createMeta(name, columnNames);
        }

        ITableMetaData createMeta(final String name, final List<String> columnNames) {
            Column[] columns = null;
            if (columnNames != null) {
                columns = new Column[columnNames.size()];
                for (int i = 0; i < columnNames.size(); i++)
                    columns[i] = new Column(columnNames.get(i), DataType.UNKNOWN);
            }
            return new DefaultTableMetaData(name, columns);
        }

        public int getRowCount() {
            return data.size();
        }

        public ITableMetaData getTableMetaData() {
            return meta;
        }

        public Object getValue(final int row, final String column) throws DataSetException {
            if (data.size() <= row)
                throw new RowOutOfBoundsException("" + row);
            return data.get(row).get(column.toUpperCase());
        }

        public void addRow(final Map<String, Object> values) {
            data.add(convertMap(values));
        }

        Map<String, Object> convertMap(final Map<String, Object> values) {
            final Map<String, Object> ret = new HashMap<>();
            for (final Map.Entry<String, Object> ent : values.entrySet()) {
                ret.put(ent.getKey().toUpperCase(), ent.getValue());
            }
            return ret;
        }

    }

    MyTable createTable(final String name, final List<Map<String, Object>> rows) {
        final MyTable table = new MyTable(name, rows.size() > 0 ? new ArrayList<>(rows.get(0).keySet()) : null);
        for (final Map<String, Object> values : rows)
            table.addRow(values);
        tables.put(name, table);
        return table;
    }

    public ITable getTable(final String tableName) throws DataSetException {
        return tables.get(tableName);
    }

    public ITableMetaData getTableMetaData(final String tableName) throws DataSetException {
        return tables.get(tableName).getTableMetaData();
    }

    public String[] getTableNames() throws DataSetException {
        return tables.keySet().toArray(new String[tables.size()]);
    }

    public ITable[] getTables() throws DataSetException {
        return tables.values().toArray(new ITable[tables.size()]);
    }

    public ITableIterator iterator() throws DataSetException {
        return new DefaultTableIterator(getTables());
    }

    public ITableIterator reverseIterator() throws DataSetException {
        return new DefaultTableIterator(getTables(), true);
    }

    public boolean isCaseSensitiveTableNames() {
        return false;
    }

}
