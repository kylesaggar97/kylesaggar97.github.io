 # CMSC320: Data Science Tutorial Project

# Installing the datasets
In this tutorial, we use several datasets.  The first is an energy dataset, which can be found at data.gov, and the other two are temperature datasets, maintained by the University of Califorinia, Berkeley.


To install the first energy dataset, go to https://catalog.data.gov/dataset/monthly-energy-consumption-by-sector, and click on the "Download" button.  The file will download an excel spreadsheet of energy data.




DataFrame Indexing:

The Dataframes have been updated, so the index is a DateTime object.  This makes it extremely easy to index/filter the dataframe by date.  For example,
	
	df['1990'] 					--> 		Selects all rows where the year equals 1990.
	df['1990':'1995'] 			--> 		Selects all rows in between 1990 and 1995, inclusive.
	df['2003-01': '2003-05'] 	--> 		Selects all rows in between January 2003 and May 2003, inclusive.

This is very powerful for filtering/selecting/indexing certain rows.