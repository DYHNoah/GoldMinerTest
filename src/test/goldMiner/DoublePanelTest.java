package test.goldMiner;

import goldMiner.GoldData;
import junit.framework.TestCase.*;
import org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import goldMiner.SoloPanel;
import goldMiner.DoublePanel;
import goldMiner.Setting;
import goldMiner.SoloMode;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

import org.junit.rules.ErrorCollector;
/**
* SoloPanel Tester.
*
* @author <Authors name>
* @since <pre>ʮ�� 16, 2021</pre>
* @version 1.0
*/
public class DoublePanelTest {
    DoublePanel doublePanel;
    Setting setting;
    SoloMode soloMode;
    ArrayList<GoldData> goldSets;
    int number,weight;

    @Rule
    public ErrorCollector collector = new ErrorCollector();
    @Before
public void before() throws Exception {
    doublePanel = new DoublePanel();
    setting = new Setting();
    soloMode = new SoloMode();
    number=setting.getMyClass();
    weight=SoloMode.getWeight();
}

@After
public void after() throws Exception {
	doublePanel = null;
    setting = null;
    soloMode = null;
}

/**
*
* Method: initData()
 * ��ʼ�����Ӻ��ڿ󹤵�λ��
*
*/
@Test
public void testInitData() throws Exception {
//TODO: ��û�в����ҵ�λ��
    int times=50;
    while(times<=0){
    	doublePanel.initData();
        if(number>1 && number<5){
            assertEquals(10+number-1,doublePanel.getGoldSets().size());
        }
        else if(number<1){
            assertEquals(0,doublePanel.getGoldSets().size());
        }
        else {
            assertEquals(14,doublePanel.getGoldSets().size());
        }
        times-=1;
    }
}

/**
*
* Method: releaseClaw()
* �ͷ�צ��
*/
@Test(timeout = 2000) @SuppressWarnings("all")
public void testReleaseClaw() throws Exception {
//TODO: Test goes here...
        int angle = 0;//angle��ʾ��ת�ĽǶ�
        SoloPanel.getGoldSets().clear();//�������
        while (angle <= 401) {//���Ƕ�Ϊ401,��֤���Ա���ȫ������
            int rotate = angle;//��ǰѭ������ת�Ƕȴ���
            while (rotate >0) {//��rotateΪ0������bug
                assertTrue(doublePanel.isClawSpin());//ȷ������צ����ת��֧��
                doublePanel.mining();
                rotate = rotate - 1;
            }
            doublePanel.calculate_k_b();//����ת���Ƕ�Ϊangle��ʱ�򣬳�����
            if (Double.isInfinite(doublePanel.getK()) || Double.isNaN(soloPanel.getK())) {
                System.out.println("K:"+doublePanel.getK());
                fail();
            }
            while (!soloPanel.isBackclaw()) {//��ͣ���������ƶ�������ֱ�������½��߽������;
                double beforeClawX = soloPanel.getClawX();//��¼�½�֮ǰ��X���ꡣ
                soloPanel.releaseClaw();//����һ�������ƶ��ķ�����
                if (beforeClawX <= soloPanel.getClawX0())
                    assertEquals(beforeClawX - 2, soloPanel.getClawX(), 0.1);
                else
                    assertEquals(beforeClawX + 2, soloPanel.getClawX(), 0.1);
                assertEquals(soloPanel.getClawY0() + soloPanel.getK() * (soloPanel.getClawX() - soloPanel.getClawX0()), soloPanel.getClawY(), 1);
                if (soloPanel.getClawY() >= 800 || soloPanel.getClawX() <= 0 || soloPanel.getClawX() >= soloMode.getWeight()) {
                    soloPanel.setClawStretch(false);
                    soloPanel.setBackclaw(true);
                }
            }
            soloPanel.restoration();//��λ
            angle = angle + 1;//��һ�νǶȼ�һ��
        }

}

/**
*
* Method: withdrawClaw()
* �ջ�צ��
*/
@Test(timeout = 1000)//����ִ��ʱ��Ϊ2s ��������bug һ����withdrawclaw ����if ��һ������ѭ�� �ջ�����������
public void testWithdrawClaw() throws Exception {
//TODO: Test goes here...
    int angle = 0;//angle��ʾ��ת�ĽǶ�
    SoloPanel.getGoldSets().clear();//�������
    while (angle <= 401) {//���Ƕ�Ϊ401��������������
        int rotate = angle;//��ǰѭ������ת�Ƕȴ���
        while (rotate >= 0) {
            assertTrue(soloPanel.isClawSpin());//ȷ������צ����ת��֧��
            soloPanel.mining();
            rotate = rotate - 1;
        }
        soloPanel.calculate_k_b();//����ת���Ƕ�Ϊangle��ʱ�򣬳�����
        while (!soloPanel.isBackclaw()) {//��ͣ���������ƶ�������ֱ�������½��߽������;
            soloPanel.mining();//����һ�������ƶ��ķ�����
        }
        System.out.println("clawX0:" + soloPanel.getClawX0());
        System.out.println("clawY0:" + soloPanel.getClawY0());
        while (!soloPanel.isWithdrawSucceed()) {//���Թ����Ƿ��ܻص���ʼλ�ã�������
            double beforeClawX = soloPanel.getClawX();//��¼����֮ǰ��X���ꡣ
            System.out.println("beforeclawX:" + soloPanel.getClawX());
            System.out.println("beforeclawY:" + soloPanel.getClawY());//��ӡ�����ƶ�֮ǰ������
            soloPanel.withdrawClaw();//����һ�������ƶ��ķ�����
            System.out.println("afterclawX:" + soloPanel.getClawX());
            System.out.println("afterclawY:" + soloPanel.getClawY());//��ӡ�����ƶ�֮�������
            if (beforeClawX <= soloPanel.getClawX0())
                assertEquals(beforeClawX + soloPanel.getStretchSpeed(), soloPanel.getClawX(), 0.1);
            else
                assertEquals(beforeClawX - soloPanel.getStretchSpeed(), soloPanel.getClawX(), 0.1);
            assertEquals(soloPanel.getClawY0() + soloPanel.getK() * (soloPanel.getClawX() - soloPanel.getClawX0()), soloPanel.getClawY(), 1);
            System.out.println(soloPanel.isClawStretch());
            System.out.println("--------------------------------");
        }
        soloPanel.restoration();
        angle = angle + 1;
    }

}

/**
*
* Method: restoration()
*
*/
@Test
public void testRestoration() throws Exception {
//TODO: Test goes here...
    soloPanel.restoration();
    assertEquals(580,soloPanel.getClawX(),0);
    assertEquals(230,soloPanel.getClawY(),0);
    assertEquals(weight/2-20,soloPanel.getClawX0(),0);
    assertEquals(130, soloPanel.getClawY0(),0);
    assertEquals(1, soloPanel.getClawSpeed());
    assertEquals(3, soloPanel.getStretchSpeed());
    assertEquals(true, soloPanel.isClawSpin());
    assertEquals(true, soloPanel.isClawStretch());
    assertEquals(false, soloPanel.isBackclaw());
    assertEquals(false, soloPanel.isGrabSucceed());
}

/**
 *
 *
 *
 */
@Test
public void testSpeed(){
    int time = 0;
    while(time <= 100) {
        int expectStrechSpeed = soloPanel.getStretchSpeed();
        //ģ��צ�Ӵ���׼��״̬
        for(int number = time; number >= 0; --number) {
            soloPanel.mining();
        }
        //��ȡ����צʱצ�ӵ�λ��
        double x1 = soloPanel.getClawX();
        double y1 = soloPanel.getClawY();
        //ģ���צ��20ms
        soloPanel.calculate_k_b();
        soloPanel.mining();
        //��ȡ20ms��צ�ӵ�λ��
        double x2 = soloPanel.getClawX();
        double y2 = soloPanel.getClawY();
        //����צ�ӵ��ٶ�
        double speed = Math.sqrt((x2-x1)*(x2-x1) + (y1-y2)*(y1-y2));
        //�ж��Ƿ���Ԥ�ڵ��ٶ���ͬ���������0
        try{ assertEquals(expectStrechSpeed, speed, 0.1); }catch (Throwable t){
            collector.addError(t); }
        soloPanel.restoration();
        ++time;
    }
}


/**
*
* Method: updateGrade(GoldData gold)
*
*/
@Test
public void testUpdateGrade() throws Exception {
//TODO: Test goes here...
    GoldData gold = new GoldData();
    updateGradeFunction(gold,1,100);
    assertEquals(150,setting.getGrade());
    updateGradeFunction(gold,2,100);
    assertEquals(180,setting.getGrade());
    updateGradeFunction(gold,3,100);
    assertEquals(200,setting.getGrade());
    updateGradeFunction(gold,4,100);
    assertEquals(300,setting.getGrade());
    updateGradeFunction(gold,5,100);
    assertEquals(125,setting.getGrade());
}
public int updateGradeFunction(GoldData goldData,int kind,int initGrade) throws Exception{
    setting.setGrade(initGrade);
    goldData.setKind(kind);
    soloPanel.updateGrade(goldData);
    return setting.getGrade();
}
/**
*
* Method: pass()
*
*/
@Test
public void testPass() throws Exception {
//TODO: Test goes here...
//    �ж���ǰ�����Ƿ����Ŀ��֣�������ͨ��
    for(int grade=setting.getTargetScore()-200;grade<setting.getTargetScore()+200;grade++){
        setting.setGrade(grade);
        if(setting.getGrade()>=setting.getTargetScore()){
            assertTrue(soloPanel.pass());
        }
    }
//    ��ʱ��������ͨ��������С�ڲ�ͨ��
    setting.setTime(60020);
    for(int grade=setting.getTargetScore()-200;grade<setting.getTargetScore()+200;grade++){
        setting.setGrade(grade);
        if(setting.getGrade()>=setting.getTargetScore()){
            assertTrue(soloPanel.pass());
        }else{
            assertFalse(soloPanel.pass());
        }
    }
//    �ж�û�н���Ҳͨ��
//    goldSets = soloPanel.getGoldSets();
//    setting.setGrade(0);
//    setting.setTime(60020);
////    ����û�н���
//    for(int i=goldSets.size()-1;i>=0;i--){
//        goldSets.remove(i);
//        if(i>0){
//            assertFalse(soloPanel.pass());
//        }else{
//            assertTrue(soloPanel.pass());
//        }
//    }

}

/**
*
* Method: gameOver()
*
*/
@Test
public void testGameOver() throws Exception {
//TODO: Test goes here...
    int currentTime = 0;
//    ǰ��60s����ģ��ʱ������
    while(!soloPanel.overtime(currentTime)){
        setting.setTime(setting.getTime() + 20);
        currentTime=setting.getTime();
    }
   for(int currentClass =1; currentClass<10; currentClass++ ){
       setting.setMyClass(currentClass);
       setting.setTargetScore(setting.getTargetScore()+200);
       for(int grade=setting.getTargetScore()-200;grade<setting.getTargetScore()+200;grade++){
           setting.setGrade(grade);
           if(setting.getGrade()<setting.getTargetScore()){
               assertTrue(soloPanel.gameOver());
           }else{
               assertFalse(soloPanel.gameOver());
           }
       }
   }
}

/**
*
* Method: overtime(int time)
*
*/
@Test
public void testOvertime() throws Exception {
//TODO: Test goes here...
//    �߽����
    for(int i = 50000;i<70000;i+=1000){
        if(i<=60000){
            assertFalse(soloPanel.overtime(i));
        }else{
            assertTrue(soloPanel.overtime(i));
        }
    }
}

@Test(timeout = 2000) @SuppressWarnings("all")
public void testGrabSucceed() {
    int angle = 0;//angle��ʾ��ת�ĽǶ�
    int oralSum = 0;//��ʼ�����Ӽ�¼Ϊ0
    for(int i=0;i<soloPanel.getGoldSets().size();i++){
        GoldData gold = SoloPanel.getGoldSets().get(i);
        soloPanel.updateGrade(gold);
    }
    int targetSum  = setting.getGrade();//��ȡĿ�����
    System.out.println("targetScore:"+targetSum);//Ŀ�������ӡ
    setting.setGrade(0);//��������ʼΪ0
    while (angle < 401)//�����תangle���Ƕ�
    {
        int rotate = angle;
        while(rotate>=0){//��ǰ��ת��rotate�Ƕ�������ѭ����
            soloPanel.mining();//ѭ��һ�Σ�ƫתһ���Ƕȡ�
            rotate = rotate - 1;
        }
        soloPanel.calculate_k_b();//����ת����ǰ�Ƕ�Ϊangle��ʱ�򣬳�����
        while (soloPanel.isClawStretch()) {//clawStretch ��ʼΪtrue������ �����߽�����Ϊfalse�����ջ�
            //��ͣ���������ƶ��������ѵ����ӻ򵽴��½��߽������;
            soloPanel.mining();//����һ�������ƶ��ķ�����
        }
        if(!soloPanel.isBackclaw()){
            assertTrue(soloPanel.isGrabSucceed());//ȷ����־λ�ı�
            if(soloPanel.getBeGrab()>=soloPanel.getGoldSets().size()||soloPanel.getBeGrab()<0){
                fail();
            }
            GoldData curGold=SoloPanel.getGoldSets().get(soloPanel.getBeGrab());//��ȡ��ǰץ�����ӵĶ���
            while(!soloPanel.isClawSpin()){//������ת״̬�������������ջ��ٴ�����ת״̬��
                soloPanel.mining();//��ǰ����԰���ͨ������ض�����ı��ջ������ٶȵ�ѭ����
                if(soloPanel.isClawSpin()){
                    break;
                }//���ĳ�����Ž��������󵽴�߽��ջأ��򲻽��������жϣ���Ϊ�ջغ�״̬�����ͻ�䡣
                assertEquals(SoloPanel.getGoldSets().get(soloPanel.getBeGrab()).getGoldX()+25,(int)soloPanel.getClawX());
                assertEquals(SoloPanel.getGoldSets().get(soloPanel.getBeGrab()).getGoldY(),(int)soloPanel.getClawY());
                //�ȽϽ��ӵ��ƶ��Ƿ����Թ���Ϊ����ϵ�ƶ�
                if(curGold.getKind()==5) assertEquals(1,soloPanel.getStretchSpeed());
                else if(curGold.getGoldSize()>=100) assertEquals(2,soloPanel.getStretchSpeed());
                else if(curGold.getKind()==4) assertEquals(4,soloPanel.getStretchSpeed());
                else assertEquals(3,soloPanel.getStretchSpeed());
                //���ٶȸı�����ж�
            }
            oralSum = oralSum + setting.getGrade();//�ۼӽ�Ǯ
            setting.setGrade(0);//����ͨ�أ�ÿ�γ�����Ǯ����ʼ��Ϊ0��
        }
        soloPanel.restoration();//��λ
        angle = angle + 1;
    }
    System.out.println("finallyScore:"+oralSum);
    assertEquals(targetSum,oralSum);//�ж����н���ץ�����Ľ�Ǯ���Ƿ����㡣
}

@Test
public void testClawSpinChangeDirection() {
    for(int time = 20000; time >= 0; --time) {
        int beforeClawSpeed = soloPanel.getClawSpeed();
        assertTrue(soloPanel.isClawSpin());
        soloPanel.mining();
        if (soloPanel.getClawX() != 480.0D && soloPanel.getClawX()!= 680.0D) {
            assertEquals((long)beforeClawSpeed, (long)soloPanel.getClawSpeed());
        } else {
            assertEquals((long)(beforeClawSpeed * -1), (long)soloPanel.getClawSpeed());
        }
    }
}
}
