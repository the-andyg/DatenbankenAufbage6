package org.example;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class ResultFormatter {

    public String processResultSet(ResultSet rs) throws Exception
    {
        StringBuilder sb = new StringBuilder();

        ResultSetMetaData rsmd = rs.getMetaData();
        int totalCols = rsmd.getColumnCount();
        String[] colLabels = new String[totalCols];
        int[] colWidths = new int[totalCols];
        String[] colTypes = new String[totalCols];

        //Labels
        for(int i = 0; i < totalCols; i++)
        {
            colWidths[i] = rsmd.getColumnDisplaySize(i+1);
            colLabels[i] = rsmd.getColumnLabel(i+1);
            if(colLabels[i].length() > colWidths[i])
            {
                colLabels[i] = colLabels[i].substring(0, colWidths[i]);
            }
            sb.append(String.format("| %-" + colWidths[i] + "s ", colLabels[i]));
        }

        sb.append("\n");

        //Types
        for(int i = 0; i < totalCols; i++)
        {
            colTypes[i] = rsmd.getColumnTypeName(i+1);
            sb.append(String.format("| %-" + colWidths[i] + "s ", colTypes[i]));
        }

        sb.append("\n");


        String horizontalLine = getHorizontalLine(colWidths);
        sb.append(horizontalLine);
        while(rs.next())
        {
            for(int i = 0; i < totalCols; i++)
            {
                sb.append(String.format("| %"+ colWidths[i] +"s ", rs.getString(i+1)));
            }
            sb.append("\n");

        }

        return sb.toString();
    }

    public String getHorizontalLine(int[] colCounts)
    {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < colCounts.length; i++)
        {
            sb.append("+");
            for(int j = 0; j < colCounts[i] + 2; j++)
            {
                sb.append("-");
            }
        }
        sb.append("+\n");

        return sb.toString();
    }
}
