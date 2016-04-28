package test;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

import weka.classifiers.*;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.MetaCost;
import weka.classifiers.meta.RotationForest;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.Utils;
import weka.core.converters.ArffSaver;
import weka.core.converters.C45Loader;
import weka.core.converters.C45Saver;
import weka.gui.beans.PredictionAppender;
import weka.filters.*;
import weka.filters.supervised.instance.Resample;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.unsupervised.attribute.PrincipalComponents;

//String classifier_name[]={"C45","ANN","KNN","NaiveBayes","BayesNet","LibSVM","CostSensitive","MetaCost","RotationForest","DecisionTable","LADTree","RandomForest","SimpleCart","LWL","Logistic","SMO","VotedPerceptron","JRip","NBTree","KStar","PART","BFTree","SPegasos"};

public class WEKA_Tool {

	public Classifier Cf_Model;
	public Evaluation eval;
	public int classIndex;
	public int class_num;
	
	public Instances instancesTrain;	
	public Instances instancesTest;
	
	public void Save_predictions(String path,String filename) throws IOException
	{
	
        FastVector result = eval.predictions();
        Object[] s = result.toArray();      
           
		StringBuffer file = new StringBuffer();
		for (int i = 0; i < s.length; i++)
			file.append(s[i].toString() + "\r\n");
        CommonFunction.Save_File(path,filename, file.toString());
        
	}
	
	
	public void Progress(String Tr_path,String Ts_path, String TrainName, String TestName) throws Exception
	{
		File TrainData = new File(Tr_path + TrainName);
		File TestData = new File(Ts_path + TestName);

		C45Loader c45TrainLoader = new C45Loader();
		C45Loader c45TestLoader = new C45Loader();
		c45TrainLoader.setFile(TrainData);
		c45TestLoader.setFile(TestData);

		instancesTrain = c45TrainLoader.getDataSet();	
		instancesTest = c45TestLoader.getDataSet();
		this.classIndex = instancesTest.numAttributes() - 1;
		instancesTrain.setClassIndex(this.classIndex);
		instancesTest.setClassIndex(this.classIndex);	
		this.class_num = instancesTest.numClasses();
		
	}
	
	public void Build_classifier(String Tr_path,String Ts_path, String TrainName, String TestName, String ClassifierName, String set_optionClassifier) throws Exception
	{
		
		switch(ClassifierName)
		{
			case "C45"://"-C 0.25 -M 2"
				Cf_Model = new J48();
				break;
			case "ANN"://"-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a"
				Cf_Model = new MultilayerPerceptron();
				break;
			case "KNN"://"-K 3 -W 0"
				Cf_Model = new IBk();
				break;
			case "NaiveBayes":
				Cf_Model = new NaiveBayes();
				break;
			case "BayesNet"://"-D -Q weka.classifiers.bayes.net.search.local.K2 -- -P 1 -S BAYES -E weka.classifiers.bayes.net.estimate.SimpleEstimator -- -A 0.5"
				Cf_Model = new BayesNet();
				break;			
			case "LibSVM"://"-C 8 -G 0.03125"
				Cf_Model = new LibSVM();
				break;
			case "CostSensitive"://"-cost-matrix \"[0.0 1.0; 11.0 0.0]\" -M -S 1 -W weka.classifiers.trees.J48 -- -C 0.25 -M 2"
				Cf_Model = new CostSensitiveClassifier();
				break;
			case "MetaCost"://"-cost-matrix \"[0.0 1.0; 11.0 0.0]\" -I 10 -P 100 -S 1 -W weka.classifiers.trees.J48 -- -C 0.25 -M 2"
				Cf_Model = new MetaCost();
				break;	
			case "RotationForest"://"-G 3 -H 3 -P 25 -F \"weka.filters.unsupervised.attribute.PrincipalComponents -R 1.0 -A 5 -M -1\" -S 1 -I 10 -W weka.classifiers.trees.J48 -- -C 0.25 -M 2"
				Cf_Model = new RotationForest();
				break;
			
			case "DecisionTable"://"-X 1 -S \"weka.attributeSelection.BestFirst\" -D 1 -N 5"
				Cf_Model = new weka.classifiers.rules.DecisionTable();
				break;		
			case "LADTree"://"-B 10"
				Cf_Model = new weka.classifiers.trees.LADTree();
				break;
			case "RandomForest"://"-I 10 -K 0 -S 1"
				Cf_Model = new weka.classifiers.trees.RandomForest();
				break;	
			case "SimpleCart"://"-S 1 -M 2.0 -N 5 -C 1.0"
				Cf_Model = new weka.classifiers.trees.SimpleCart();
				break;	
			case "LWL"://"-U 0 -K -1 -R first-last\" -W weka.classifiers.trees.DecisionStump"
				Cf_Model = new weka.classifiers.lazy.LWL();
				break;
			case "Logistic"://"-R 1.0E-8 -M -1"
				Cf_Model = new weka.classifiers.functions.Logistic();
				break;		
			case "SMO"://"-C 1.0 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel\" -C 250007 -E 1.0"
				Cf_Model = new weka.classifiers.functions.SMO();
				break;	
			case "VotedPerceptron"://" -I 1 -E 1.0 -S 1 -M 10000"
				Cf_Model = new weka.classifiers.functions.VotedPerceptron();
				break;		
				
			case "JRip"://"-F 3 -N 2.0 -O 2 -S 1"
				Cf_Model = new weka.classifiers.rules.JRip();
				break;					
			case "NBTree"://""
				Cf_Model = new weka.classifiers.trees.NBTree();
				break;		
			case "KStar"://"-B 20 -M a"
				Cf_Model = new weka.classifiers.lazy.KStar();
				break;		
			case "PART"://"-M 2 -C 0.25 -Q 1"
				Cf_Model = new weka.classifiers.rules.PART();
				break;	
			case "BFTree"://"-S 1 -M 2 -N 5 -C 1.0 -P POSTPRUNED"
				Cf_Model = new weka.classifiers.trees.BFTree();
				break;	
			case "SPegasos"://"-F 0 -L 1.0E-4 -E 500"
				Cf_Model = new weka.classifiers.functions.SPegasos();
				break;			
			default:
				break;
		}
		
		Cf_Model.setOptions(weka.core.Utils.splitOptions(set_optionClassifier));
		Progress(Tr_path,Ts_path,TrainName,TestName);
		Cf_Model.buildClassifier(instancesTrain);
		
		eval = new Evaluation(instancesTrain);
		eval.evaluateModel(Cf_Model, instancesTest);
	}
	
	
	public void Save_eval_result(String path, String File_name) throws IOException
	{		
		String result="";
		for(int i=0;i<class_num;i++)
		{
			double TP,FN,FP,TN;	
			TP = eval.numTruePositives(i);
			FN = eval.numFalseNegatives(i);
			FP = eval.numFalsePositives(i);
			TN = eval.numTrueNegatives(i);
			
			result+="     TP     FN     FP     TN    TPR    FPR    PPV    ACC Fscore    MCC    ROC  Class\n";
			result+=Utils.doubleToString(TP,7,0);
			result+=Utils.doubleToString(FN,7,0);
			result+=Utils.doubleToString(FP,7,0);
			result+=Utils.doubleToString(TN,7,0);
			result+="  "+Utils.doubleToString(eval.truePositiveRate(i),5,3);	
			result+="  "+Utils.doubleToString(eval.falsePositiveRate(i),5,3);	
			result+="  "+Utils.doubleToString(eval.precision(i),5,3);	
			result+="  "+Utils.doubleToString((TP+TN)/(TP+TN+FP+FN),5,3);	
			result+="  "+Utils.doubleToString(eval.fMeasure(i),5,3);	
			result+="  "+Utils.doubleToString((TP*TN-FN*FP)/Math.sqrt((TP+FP)*(TP+FN)*(FP+TN)*(FN+TN)),5,3);	
			result+="  "+Utils.doubleToString(eval.areaUnderROC(i),5,3);	
			result+=Utils.doubleToString(i,7,0)+"\n";
		}
		System.out.println(result);
		CommonFunction.Save_File(path, File_name, result);
	}
	
	public void Save_model(String path,String filename) throws Exception
	{
		weka.core.SerializationHelper.write(path+"\\"+filename, Cf_Model);	
	}
	public void Load_model(String path,String filename) throws Exception
	{
		Cf_Model = (Classifier) weka.core.SerializationHelper.read(path+"\\"+filename);
	}
	
}
