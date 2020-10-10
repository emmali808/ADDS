package com.java.adds.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConsultService {
    private   String rank_idx(String[] query_text,String[] doc_text) {
        List<String> command = new ArrayList<String>();
        command.add("python");
        command.add("main.py");
        for(int i=0;i<query_text.length;i++){
            command.add("\""+query_text[i]+"\"");
        }
        command.add("and");
        for(int i=0;i<doc_text.length;i++){
            command.add("\""+doc_text[i]+"\"");
        }
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            File dir = new File("/home/lf/桌面/qa_project/");
            builder.directory(dir);
            Process proc = builder.start();

            BufferedReader in = new BufferedReader((new InputStreamReader((proc.getInputStream()))));
            String result = in.readLine();
            in.close();

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
    public String consultReuslt(String que){
        String [] query=new String[1];
        query[0]=que;
        String[] doc =
                {"Pneumococcus is a germ ( bacterium ) which can cause pneumonia , meningitis and some other infections . Pneumonia caused by pneumococcus occurs in about 1 in 1,000 adults each year . Pneumococcal infection can affect anybody . However , young children , older people and some other groups of people are at increased risk of developing a pneumococcal infection.",
                        "If you have had a severe reaction to a previous dose of pneumococcal vaccine . A dose of vaccine may be delayed if you are ill , or your child is ill , with a high temperature ( fever ) . There is no reason to delay a dose of vaccine if you have a minor infection , or your child has a minor infection , such as a cough , cold or snuffles . The vaccine may be given to pregnant women when the need for protection is required without delay . It is safe to have if you are breast - feeding . By clicking ' Subscribe ' you agree to our Terms and conditions and Privacy policy . Thanks for your feedback . if you would like to report a specific issue with this page , please visit our feedback page.",
                        "A sore throat is the most common of all tonsillitis symptoms . You may also have a cough , high temperature ( fever ) , headache , feel sick ( nausea ) , feel tired , find swallowing painful , and have swollen neck glands . The tonsils may swell and become red . Pus may appear as white spots on the tonsils . Symptoms typically get worse over 2 - 3 days and then gradually go , usually within a week . The picture below shows inflamed tonsils . Glandular fever ( infectious mononucleosis ) is caused by a virus ( the Epstein - Barr virus ) . It tends to cause a severe bout of tonsillitis as well as other symptoms . See separate leaflet called Glandular Fever ( Infectious Mononucleosis ) for more details.",
                        "Usually not . Most throat and tonsil infections are caused by germs called viruses , although some are caused by germs called bacteria . Without tests , it is usually not possible to tell if it is a viral or bacterial infection . Antibiotics kill bacteria , but do not kill viruses . Even if a bacterium is the cause of a tonsil or throat infection , an antibiotic does not make much difference in most cases . Your body defences ( immune system ) usually clear these infections within a few days whether caused by a virus or a bacterium . Also , antibiotics can sometimes cause side - effects such as diarrhoea , rash and stomach upsets . So , most doctors do not prescribe antibiotics for most cases of tonsillitis or sore throat . An antibiotic may be advised in certain situations . For example : If the infection is severe . If it is not easing after a few days . If your immune system is not working properly ( for example , if you have had your spleen removed , if you are taking chemotherapy , etc ).",
                        "Short sight occurs when light coming from distant objects is ' overfocused ' , so that the point of focus is in front of the retina . It occurs because either the eyeball is too long , or because the cornea is too curved . Despite maximum flattening of the lens , the eye is not able to focus the light rays further back and on to the retina . Light coming from near objects requires a stronger focusing activity anyway , so in myopia light from near objects is more likely to be focused in the right place . People with short sight are not able to see distant objects clearly . Short sight or near sight mean exactly what the terms suggest . You are sighted ( you can see ) , near ( short ) distance objects . Near objects ( for example , when reading a book ) can often be seen well . This is because when looking at near objects , the light rays come into the eye going slightly outwards . These will focus further back in the eye than light rays that come in straight from distant objects . The diagram above shows the differences in focusing between a normal and a short - sighted ( myopic ) eye .",
                        "Short sight tends to happen in children and young teenagers . It often runs in families . Temporary short - sightedness can also occur with certain illnesses - for example , in diabetes .",
                        "The main symptom is a difficulty with distance vision . The earlier short sight starts , the more severe it is likely to become . By the time early adulthood is reached , the level of short sight has usually reached its peak . This means that the vision does not generally become any worse . Some children do not realise that their vision is not as good as it should be . They may be able to read books and do close work well . However , seeing distant objects such as the board at school may become difficult . They may think this is normal and not tell anyone . Schoolwork may suffer for a while before the condition is identified and treatment provided . Children usually have a routine preschool or school - entry vision check . Your child 's teacher may notice that children are having difficulties in class reading the board . If you suspect your child has problems with his or her sight , you should arrange for an eyesight test with an optician who is happy to assess children . For young children and toddlers , your GP may be able to make arrangements for a sight test . Sight tests are free for children .",
                        "A cure is the aim in many cases . Some cancers can be cured with radiotherapy alone . Sometimes radiotherapy is used in addition to another main treatment . For example : You may have surgery to remove a tumour but you may also be given a course of radiotherapy after the surgery . This aims to kill any cancer cells which may have remained following surgery . Unless treated , these may have caused a recurrence of the tumour at a later time . Radiotherapy given after surgery is called adjuvant radiotherapy . Sometimes radiotherapy is given before surgery , to reduce the size of the tumour and make it easier to remove . Radiotherapy given before surgery is called neoadjuvant radiotherapy . In some cases , radiotherapy and chemotherapy are used in combination . Find out more about chemotherapy . Doctors tend to use the word remission rather than the word cured . Remission means there is no evidence of cancer following treatment . If you are in remission , you may be cured . However , in some cases , a cancer returns months or years later . This is why some doctors are reluctant to use the word cured . If a cure is not realistic , with treatment it is often possible to limit the growth or spread of the cancer so that it progresses less rapidly . This may keep you free of symptoms for some time . Even if a cure is not possible and the outlook is poor , a course of radiotherapy may be used to reduce the size of a cancer . This may ease symptoms such as pain or pressure symptoms from the tumour . This is called palliative radiotherapy .",
                        "The treatment depends on which part of the body is affected . For the joint pains , usually aspirin or ibuprofen is sufficient . They settle in a few weeks . For heart problems , a specialist doctor may need to prescribe medicines that relieve the strain on the heart . These are medicines like ' water ' tablets ( diuretics ) , angiotensin - converting enzyme ( ACE ) inhibitors and digoxin . Sometimes the damage to the heart valves is so bad that urgent heart surgery is needed . The jerky movements ( chorea ) are sometimes difficult to treat . Generally sedatives are used like diazepam . If the chorea is very severe and lasts several weeks then specialist procedures like plasmapheresis are used : this is a way of ' cleaning ' your blood by pumping it through a special machine and back into your body . Usually the antibiotic penicillin is given for ten days to make sure that none of the original bacterium , the streptococcus , is still in the body . If the heart problems are particularly bad , some people recommend penicillin until the age of 21 years at least . Rheumatic fever is one of the few conditions where bed rest is recommended , even if the person feels well enough to be up and about . They should rest until the blood tests for inflammation return to normal .",
                        "Psoriatic nail disease can be difficult to treat and so can continue to cause discomfort . The appearance of the affected nails can also sometimes cause distress . The treatment of severe psoriatic nail disease is now improving with modern medicines . Psoriatic nail disease can also be mild , not need any treatment and can be hidden with nail varnish . By clicking ' Subscribe ' you agree to our Terms and conditions and Privacy policy . Thanks for your feedback . if you would like to report a specific issue with this page , please visit our feedback page .",
                        "Most people who develop whooping cough make a full recovery . However , it can be a miserable illness , as the relentless bouts of coughing can be distressing . The total length of the full illness is commonly 6 - 8 weeks but often lasts three months or more . Severe complications and death are uncommon but occur mostly in babies under 6 months of age . Serious illness is less common in older children and adults . Once recovered , you are usually then immune to whooping cough and so are very unlikely to get it again . By clicking ' Subscribe ' you agree to our Terms and conditions and Privacy policy . Thanks for your feedback . if you would like to report a specific issue with this page , please visit our feedback page ."};

        String result = rank_idx(query,doc);
        if(result!="error") {
            List<String> index = new ArrayList<String>();
            index = getIndex(result);
            Integer flag = index.lastIndexOf("0");
            if (flag == -1) {
                return "error";
            }
            return doc[flag];
        }
        else {
            return "rank_idx_error";
        }

    }
    public List<String> getIndex(String str){
//        String str="([[1, 0]], [[14.567728042602539, 21.839862823486328], [-23.143768310546875, -27.353540420532227], [5.182803630828857, 16.994691848754883]])";
        String regex="\\[(\\[.*\\])\\]\\,";
        String result;
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(str);
        if(matcher.find()){
            result=matcher.group(1);


            String demo = result.replace(" ","");
            String demosub = demo.substring(1,demo.length()-1);
            String demoArray[] = demosub.split(",");
            List<String> demoList = Arrays.asList(demoArray);
//            List<Integer> index=new ArrayList<Integer>();
//            for (String str1:demoList) {
//
//                index.add(Integer.valueOf(str1));
//            }
            return demoList;
        }
        return null;
    }
}
