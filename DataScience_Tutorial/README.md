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


Regression Model:

	We can now develeop a regression model for predicting temperature anomaly or data usage.  To train our model, do the following:

		To do so, call linear_model(X, Y) :- where X is a matrix of feature vectors and Y is labels corresponding to these training examples.

		Instead of modeling X as datetime objects, we model X as a matrix, where columns represent: Year|Month|Day, in that order.
		For example, a row in X would contain [2001, 6, 1] to represent 6/1/2001.  We can then train on X and Y, using this data.

	To make a prediction:

		reg = linear_model(X, Y) 		#Train our model
		reg.predict([2001, 6, 1])		#Make a prediction for 6/1/2001


