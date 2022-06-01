# Overview
Galaxy Merchant Trading application basically a combination of conversion and calculation application with dynamic number format and unit (based on user input). The only predefined format in the application is only for conversion mapping between Roman and Arabic number. But in intergalactic trading, mapping between galaxy number and roman number is based on user input.  

# Components
Based on the requirements there are several main functionalities
* Galaxy number to Roman number conversion
* Roman number to Arabic number conversion
* Roman number validator
* Six type of input / query processor:  
  * Galaxy number to roman processor
  * Galaxy unit to number processor
  * Galaxy number calculation processor
  * Galaxy credit calculation processor
  * Galaxy credit comparison processor
  * Galaxy number comparison processor 
* Multiline text processor which handling input validation
* Implementation using Design pattern for easy maintability and extendibility 

# Implementation
The application is developed using Java 11, Springboot framework and maven dependency management.
Most of the functionality is created from scratch, some method libraries help for String checking to handle error. 
Unit testing also provided to ensure application can handle some scenarios.

### Factory Design Pattern
To achieve extendibility, I use this Factory design pattern.  
Each specific purpose TextProcessor have to implements TextProcessor abstract.  
TradingService will collect processor configuration which stored in database (contains Patttern and ProcessorName) and ask TextProcessorFactory to provide the correct TextProcessor.
![image](https://user-images.githubusercontent.com/58011539/171343644-798ad198-a02a-4b0c-8455-e34b23457a0b.png)

TradingService also will iterate all Processors and generate the output by handling all codition (i.e: matching pattern, invalid value, exception occure).
Let say, when processing a text, the first processor is invalid, the next proccess will try to handle it, until its valid or until there is no processor left. 
![image](https://user-images.githubusercontent.com/58011539/171347490-d6226e7e-0932-4616-94e0-8d8a5ef14af7.png)

### How to add new Processor or Pattern
There are two approach based on conditions.

#### New Pattern with existing Processor Logic
1. Add new ProcessorConfig with designated (existing) ProcessorName (API service available)
![image](https://user-images.githubusercontent.com/58011539/171347788-e36fe90a-b8eb-48d1-b078-0ba277f71b13.png)

2. Execute again the input (without restart) and evaluate the output
Note: By doing that, there is a Processor that can process text using multiple pattern    

#### New Pattern with new Processor Logic
1. Coding: new ProcessorClass which implments TextProcessor abstract
![image](https://user-images.githubusercontent.com/58011539/171346380-f342918a-c05c-4f09-b97a-96b7f52652ef.png)

2. Coding: TextProcessorFactory for the new ProcessorClass
![image](https://user-images.githubusercontent.com/58011539/171346243-a2c77cdc-b93e-4b99-87bc-28324825287b.png)

3. Add ProcessorConfig in database for the new ProcessorClass

### Database
I use H2 embeded database for easy deployment (since this application only a demo)
When we start the application, it will load default ProcesserConfig from file to H2, so the application is ready to process the current requirement. 
![image](https://user-images.githubusercontent.com/58011539/171348668-2e683fd8-e5fc-4631-9ef9-884c7458679f.png)
Don't forget to change the H2 file location according to yout environenment
![image](https://user-images.githubusercontent.com/58011539/171349009-2b0d2b60-68d7-4e09-8ef5-4a4324051052.png)


# Run The Application
Below are the steps to running the application. We provide two approaches to running the application
### Run using Java -jar
1.	Clone or download the source code from https://github.com/mbanx/galaxy-trading.git
2.	Ensure maven is installed and configured
3.	Change to project root directory
4.	Run command: mvn install. It will build the project and download all necessary dependencies
5.	Run command java -jar <application.jar>

### Run in IDE
1.	Clone or download the source code from https://github.com/mbanx/galaxy-trading.git
2.	Ensure maven is installed and configured
3.	Configure project to download all necessary dependencies
4.	You can direction run the main class org.mbanx.challenge.galaxy.Application.java

Once the application started you can access the swagger-ui using browser **http://localhost:8080/swagger-ui.html** and access endpoint **POST:/trading/queries** and write query on body textbox.

### Swagger input example
![alt text](https://github.com/mbanx/galaxy-trading/blob/master/img/input.png?raw=true)

![alt text](https://github.com/mbanx/galaxy-trading/blob/master/img/output.png?raw=true)
