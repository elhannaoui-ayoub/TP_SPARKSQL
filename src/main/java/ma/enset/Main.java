package ma.enset;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import static org.apache.spark.sql.functions.*;


public class Main {


        public static void main(String[] args) {

            SparkSession ss = SparkSession.builder().appName("SparkSQL").master("local[*]").getOrCreate();
            Dataset<Row> df = ss.read().option("header", true).option("inferSchema", true).csv("incidents.csv");

            df.createOrReplaceTempView("incidents");
            Dataset<Row> incidentsParServiceSql = ss.sql("SELECT service, COUNT(*) AS nombre_incidents FROM incidents GROUP BY service");
            incidentsParServiceSql.show();

            Dataset<Row> incidentsDs = df.as("incidents_ds");
            Dataset<Row> anneesPlusIncidentsDs = incidentsDs.groupBy(year(col("date")).alias("annee"))
                    .agg(count("*").alias("nombre_incidents"))
                    .orderBy(desc("nombre_incidents"))
                    .limit(2);
            anneesPlusIncidentsDs.show();

        }

}