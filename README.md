# Large scale Performance metrics gathering
--------------

> Team project developed for CMPE2803 (Spring 2015)
> at San Jose State University
 - Title: Large scale Statistics collection of VMWare VM and VHost performance metrics
 
##Features supported
--------------
 - Performing collection of system data (metrics, logs) to identify workloads on system elements (jobs,
hosts, guests etc) and store them in noSQL Database such as ElasticSearch. Data rate can reach 100,000 per minutes.
Monitor Hosts and collect the following metrics (Per Guest Level CPU, Memory, Threads, I/O, VMotion)
 - Presenting and/or visualizing the outcomes of the above logs/data in a simple manner.
 - Used Logstach, ElasticSearch and Kibana combination.
 - Logstash grok command used for formating the collected data.
 
##Basic Configuration
--------------
* VMWare ESXi installed on team data center.
* VM running on Ubuntu 32bit.
* Performance manager java application running as start up process in each VM.
* Logstash running on each VM.
* Centralized server with ElasticSearch and Kibana (AWS instance)

##Tools being used
--------------
* VMWare tools installed on each VM.
* VSphere Management client
* VMWare Infrastructure(VI) Apis

##Project Component Architecture diagram
----------------------------------------
![Design](/screenshot/Design.png?raw=true)
