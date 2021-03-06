/**
* Name: localizeiris
* Author: ben
* Description: 
* Tags: Tag1, Tag2, TagN
*/

model localizeiris

global {
	file f_IRIS <- file("../../data/Rouen_iris.csv");

	// String constants
	file iris_shp <- file("../../data/shp/Rouen_iris_number.shp");
	
	//name of the property that contains the id of the census spatial areas in the shapefile
	string stringOfCensusIdInShapefile <- "CODE_IRIS";

	//name of the property that contains the id of the census spatial areas in the csv file (and population)
	string stringOfCensusIdInCSVfile <- "iris";

	geometry shape <- envelope(iris_shp);

	init {		
		create iris_agent from: iris_shp with: [code_iris::string(read('CODE_IRIS'))];			
		
		gen_population_generator pop_gen;
		pop_gen <- pop_gen with_generation_algo "IS";  //"Sample";//"IS";

		pop_gen <- add_census_file(pop_gen, f_IRIS.path, "ContingencyTable", ",", 1, 1);			
			
		// -------------------------
		// Setup "IRIS" attribute: INDIVIDUAL
		// -------------------------

		list<string> liste_iris <- [
			"765400602","765400104","765400306","765400201",
			"765400601","765400901","765400302","765400604","765400304",
			"765400305","765400801","765400301","765401004","765401003",
			"765400402","765400603","765400303","765400103","765400504",
			"765401006","765400702","765400401","765400202","765400802",
			"765400502","765400106","765400701","765401005","765400204",
			"765401001","765400405","765400501","765400102","765400503",
			"765400404","765400105","765401002","765400902","765400403",
			"765400203","765400101","765400205"];
		pop_gen <- pop_gen add_attribute("iris", string, liste_iris, "P13_POP", int);  


		// -------------------------
		// Spatialization 
		// -------------------------
		pop_gen <- pop_gen localize_on_census(iris_shp.path);
		pop_gen <- pop_gen add_spatial_mapper(stringOfCensusIdInCSVfile,stringOfCensusIdInShapefile);

		// -------------------------			
		create people from: pop_gen number: 10000 ;
	}
}

species people {
	string iris;
	rgb color <- first(iris_agent where (each.code_iris = iris)).color;

	aspect default { 
		draw circle(4) color: color border: #black;
	}
}

species iris_agent {
	string code_iris;
	rgb color <- rnd_color(255);
	
	aspect default {
		draw shape color:color  border: #black;
	}
}

experiment Rouentemplate type: gui {
	output {
		display map scale: true type: opengl {
			species iris_agent;
			species people;
		}
	}
}
