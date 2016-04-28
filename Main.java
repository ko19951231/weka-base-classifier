package test;

import java.io.Serializable;
import java.util.Scanner;

import weka.core.Instances;
import weka.core.Utils;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		
		int fold_num = 5;
		double []avg = new double [4];
		for(int i=0;i<avg.length;i++)
			avg[i]=0;
		
		String []C = new String [8];
		C[0]="C45";
		C[1]="KNN";
		C[2]="VotedPerceptron";
		C[3]="PART";
		C[4]="RandomForest";
		C[5]="BayesNet";
		C[6]="JRip";
		C[7]="LibSVM";
		
		String []Op = new String[8];
		Op[0]="-C 0.25 -M 2";
		Op[1]="-K 3 -W 0";
		Op[2]=" -I 1 -E 1.0 -S 1 -M 10000";
		Op[3]="-M 2 -C 0.25 -Q 1";
		Op[4]="-I 10 -K 0 -S 1";
		Op[5]="-D -Q weka.classifiers.bayes.net.search.local.K2 -- -P 1 -S BAYES -E weka.classifiers.bayes.net.estimate.SimpleEstimator -- -A 0.5";
		Op[6]="-F 3 -N 2.0 -O 2 -S 1";
		Op[7]="-S 0 -K 2 -D 3 -G 0.03125 -R 0.0 -N 0.5 -M 40.0 -C 8.0 -E 0.001 -P 0.1 -seed 1 -B 1";
		
		for(int a=0;a<8;a++)
		{
			String path = System.getProperty("user.dir")+"\\source\\";
			String save_path = System.getProperty("user.dir")+"\\output\\";
			
			String result= "";
			result+="     TP     FN     FP     TN    TPR    FPR    PPV    ACC Fscore    MCC    ROC  Class\n";		

			for(int i=1;i<=fold_num;i++)
			{
				WEKA_Tool wekaTool = new WEKA_Tool();
				wekaTool.Build_classifier(path,path,"training"+String.format("%02d",i)+".names","testing"+String.format("%02d",i)+".names", C[a],Op[a]);
						
				result+=Utils.doubleToString(wekaTool.eval.numTruePositives(1),7,0);
				result+=Utils.doubleToString(wekaTool.eval.numFalseNegatives(1),7,0);
				result+=Utils.doubleToString(wekaTool.eval.numFalsePositives(1),7,0);
				result+=Utils.doubleToString(wekaTool.eval.numTrueNegatives(1),7,0);
				
				result+="\n";		
			
				avg[0]+=wekaTool.eval.numTruePositives(1);
				avg[1]+=wekaTool.eval.numFalseNegatives(1);
				avg[2]+=wekaTool.eval.numFalsePositives(1);
				avg[3]+=wekaTool.eval.numTrueNegatives(1);
			
				wekaTool.Save_predictions(save_path, C[a]+"-predict"+i+".txt");	
				wekaTool.Save_eval_result(save_path, C[a]+"-result"+i+".txt");
				wekaTool.Save_model(save_path, C[a]+"-Model"+i+".model");
				wekaTool = null;
				System.out.println(C[a]+" fold"+i+" Complete!!");
			}
			
			result+="\n";	
			result+=Utils.doubleToString(avg[0]/fold_num,7,0);
			result+=Utils.doubleToString(avg[1]/fold_num,7,0);
			result+=Utils.doubleToString(avg[2]/fold_num,7,0);
			result+=Utils.doubleToString(avg[3]/fold_num,7,0);
			result+="\n";
			CommonFunction.Save_File(save_path, C[a]+"-Eval.txt",result);
			
			System.out.println(C[a]+" Complete!!");		
			System.out.println(C[a]+" Save Complete!!");		
		}
		System.out.println("All Complete!!");
	}
	
}
