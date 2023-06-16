# Replication package for the Bachelor's thesis "Evaluating .NET MAUI as a replacement for native Android mobile application development with focus on performance"

## Treatment recreations: Recreation of each application evaluated in the thesis

The folder *code_files* contains the code files containing the most important code files used to create each application.
The required software to reproduce the applications can be found in each sub-folder's README.


### Structure

* *MAUI*: contains the files necessary to recreate the .NET MAUI project
  * *README.md*: contains a simple guide for how to set up and run the project
* *native*: contains the files necessary to recreate the native project
  * *README.md*: contains a simple guide for how to set up and run the project

## Graph Creation: Creating the graphs based on the results of the Mapping Study, as well as the Empirical Study

The folder *graph_creation_mapping_and_empirical* demonstrates the use of `matplotlib` to genereate box- and bar plots for the data collected during both the mapping study as well as the empirical study performed.

### Structure

The contents of that folder are structured as follows:

* *data*: directory for all raw data. 
  * *extracted_data*: holds all the data extracted during the mapping study.
  * *observations*: data from the empirical observations, where each file represents one treatment and each column a combination of a task and metric
* *src*: source code
  * *extract_empirical_boxes.ipynb*: demonstration of data visualization for the empirical study using `matplotlib`
  * *extract_literature_bars.ipynb*: demonstration of data visualization for the mapping study using `matplotlib`
* *requirements.txt*: list of Python libraries necessary to run the Jupyter notebooks

### How to reproduce

In order to reproduce the results, make sure [Python 3.10](https://www.python.org/downloads/) is available on your machine. Install all requirements via `python -m pip install -r requirements.txt`. Then execute the Jupyter notebooks.

## Hypothesis Testing: Performance Difference between Native and Cross-Platform Applications

The folder *hypothesis_testing* demonstrates the use of hypothesis testing to investigate, whether there is a statistically significant difference in performance (measured via the two metrics CPU and RAM usage) in several different tasks when comparing native with .NET MAUI applications.

### Structure

The contents of that folder are structured as follows:

* *data*: directory for all raw data
  * *observations*: data from the empirical observations, where each file represents one treatment and each column a combination of a task and metric
  * *extracted_data*: results from the literature study informing the selection of metrics
* *src*: source code
  * *hypothesis_testing.ipynb*: demonstration of frequentist null-hypothesis testing
* *requirements.txt*: list of Python libraries necessary to run the Jupyter notebooks

### How to reproduce

In order to reproduce the results, make sure [Python 3.10](https://www.python.org/downloads/) is available on your machine. Install all requirements via `python -m pip install -r requirements.txt`. Then execute the Jupyter notebooks.

## License

Copyright © 2023 Lukas Palmqvist

This work is licensed under MIT License.