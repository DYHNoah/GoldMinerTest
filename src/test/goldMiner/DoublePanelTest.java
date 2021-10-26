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
import goldMiner.DoubleMode;

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
    DoubleMode doubleMode;
    ArrayList<GoldData> goldSets;
    int number,weight;

    @Rule
    public ErrorCollector collector = new ErrorCollector();
    @Before
public void before() throws Exception {
    doublePanel = new DoublePanel();
    setting = new Setting();
    doubleMode = new DoubleMode();
    number=setting.getMyClass();
    weight=DoubleMode.getWeight();
}

@After
public void after() throws Exception {
	doublePanel = null;
    setting = null;
    doubleMode = null;
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
        DoublePanel.getGoldSets().clear();//�������
        while (angle <= 401) {//���Ƕ�Ϊ401,��֤���Ա���ȫ������
            int rotate = angle;//��ǰѭ������ת�Ƕȴ���
            while (rotate > 0) {//��rotateΪ0������bug
                assertTrue(doublePanel.isClawSpin_1());//ȷ������צ����ת��֧��
                doublePanel.mining(1);
                rotate = rotate - 1;
            }
            doublePanel.calculate_k1_b1();//����ת���Ƕ�Ϊangle��ʱ�򣬳�����
            if (Double.isInfinite(doublePanel.getK_1()) || Double.isNaN(doublePanel.getK_1())) {
                System.out.println("K:"+doublePanel.getK_1());
                fail();
            }
            while (!doublePanel.isBackclaw_1()) {//��ͣ���������ƶ�������ֱ�������½��߽������;
                double beforeClawX = doublePanel.getClawX_1();//��¼�½�֮ǰ��X���ꡣ
                doublePanel.releaseClaw(1);//����һ�������ƶ��ķ�����
                if (beforeClawX <= doublePanel.getClawX0_1())
                    assertEquals(beforeClawX - 2, doublePanel.getClawX_1(), 0.1);
                else
                    assertEquals(beforeClawX + 2, doublePanel.getClawX_1(), 0.1);
                assertEquals(doublePanel.getClawY0_1() + doublePanel.getK_1() * (doublePanel.getClawX_1() - doublePanel.getClawX0_1()), doublePanel.getClawY_1(), 1);
                if (doublePanel.getClawY_1() >= 800 || doublePanel.getClawX_1() <= 0 || doublePanel.getClawX_1() >= doubleMode.getWeight()) {
                	doublePanel.setClawStretch_1(false);
                	doublePanel.setBackclaw_1(true);
                }
            }
            doublePanel.restoration(1);//��λ
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
    DoublePanel.getGoldSets().clear();//�������
    while (angle <= 401) {//���Ƕ�Ϊ401��������������
        int rotate = angle;//��ǰѭ������ת�Ƕȴ���
        while (rotate >= 0) {
            assertTrue(doublePanel.isClawSpin_1());//ȷ������צ����ת��֧��
            doublePanel.mining(1);
            rotate = rotate - 1;
        }
        doublePanel.calculate_k1_b1();//����ת���Ƕ�Ϊangle��ʱ�򣬳�����
        while (!doublePanel.isBackclaw_1()) {//��ͣ���������ƶ�������ֱ�������½��߽������;
        	doublePanel.mining(1);//����һ�������ƶ��ķ�����
        }
        System.out.println("clawX0:" + doublePanel.getClawX0_1());
        System.out.println("clawY0:" + doublePanel.getClawY0_1());
        while (!doublePanel.isWithdrawSucceed(1)) {//���Թ����Ƿ��ܻص���ʼλ�ã�������
            double beforeClawX = doublePanel.getClawX_1();//��¼����֮ǰ��X���ꡣ
            System.out.println("beforeclawX:" + doublePanel.getClawX_1());
            System.out.println("beforeclawY:" + doublePanel.getClawY_1());//��ӡ�����ƶ�֮ǰ������
            doublePanel.withdrawClaw(1);//����һ�������ƶ��ķ�����
            System.out.println("afterclawX:" + doublePanel.getClawX_1());
            System.out.println("afterclawY:" + doublePanel.getClawY_1());//��ӡ�����ƶ�֮�������
            if (beforeClawX <= doublePanel.getClawX0_1())
                assertEquals(beforeClawX + doublePanel.getStretchSpeed_1(), doublePanel.getClawX_1(), 0.1);
            else
                assertEquals(beforeClawX - doublePanel.getStretchSpeed_1(), doublePanel.getClawX_1(), 0.1);
            assertEquals(doublePanel.getClawY0_1() + doublePanel.getK_1() * (doublePanel.getClawX_1() - doublePanel.getClawX0_1()), doublePanel.getClawY_1(), 1);
            System.out.println(doublePanel.isClawStretch_1());
            System.out.println("--------------------------------");
        }
        doublePanel.restoration(1);
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
	doublePanel.restoration(1);
    assertEquals(580,doublePanel.getClawX_1(),0);
    assertEquals(230,doublePanel.getClawY_1(),0);
    assertEquals(weight/2-20,doublePanel.getClawX0_1(),0);
    assertEquals(130, doublePanel.getClawY0_1(),0);
    assertEquals(1, doublePanel.getClawSpeed_1());
    assertEquals(3, doublePanel.getStretchSpeed_1());
    assertEquals(true, doublePanel.isClawSpin_1());
    assertEquals(true, doublePanel.isClawStretch_1());
    assertEquals(false, doublePanel.isBackclaw_1());
    assertEquals(false, doublePanel.isGrabSucceed_1());
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
        int expectStrechSpeed = doublePanel.getStretchSpeed_1();
        //ģ��צ�Ӵ���׼��״̬
        for(int number = time; number >= 0; --number) {
        	doublePanel.mining(1);
        }
        //��ȡ����צʱצ�ӵ�λ��
        double x1 = doublePanel.getClawX_1();
        double y1 = doublePanel.getClawY_1();
        //ģ���צ��20ms
        doublePanel.calculate_k1_b1();
        doublePanel.mining(1);
        //��ȡ20ms��צ�ӵ�λ��
        double x2 = doublePanel.getClawX_1();
        double y2 = doublePanel.getClawY_1();
        //����צ�ӵ��ٶ�
        double speed = Math.sqrt((x2-x1)*(x2-x1) + (y1-y2)*(y1-y2));
        //�ж��Ƿ���Ԥ�ڵ��ٶ���ͬ���������0
        try{ assertEquals(expectStrechSpeed, speed, 0.1); }catch (Throwable t){
            collector.addError(t); }
        doublePanel.restoration(1);
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
    doublePanel.updateGrade(goldData);
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
            assertTrue(doublePanel.pass());
        }
    }
//    ��ʱ��������ͨ��������С�ڲ�ͨ��
    setting.setTime(60020);
    for(int grade=setting.getTargetScore()-200;grade<setting.getTargetScore()+200;grade++){
        setting.setGrade(grade);
        if(setting.getGrade()>=setting.getTargetScore()){
            assertTrue(doublePanel.pass());
        }else{
            assertFalse(doublePanel.pass());
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
    while(!doublePanel.overtime(currentTime)){
        setting.setTime(setting.getTime() + 20);
        currentTime=setting.getTime();
    }
   for(int currentClass =1; currentClass<10; currentClass++ ){
       setting.setMyClass(currentClass);
       setting.setTargetScore(setting.getTargetScore()+200);
       for(int grade=setting.getTargetScore()-200;grade<setting.getTargetScore()+200;grade++){
           setting.setGrade(grade);
           if(setting.getGrade()<setting.getTargetScore()){
               assertTrue(doublePanel.gameOver());
           }else{
               assertFalse(doublePanel.gameOver());
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
            assertFalse(doublePanel.overtime(i));
        }else{
            assertTrue(doublePanel.overtime(i));
        }
    }
}

@Test(timeout = 2000) @SuppressWarnings("all")
public void testGrabSucceed() {
    int angle = 0;//angle��ʾ��ת�ĽǶ�
    int oralSum = 0;//��ʼ�����Ӽ�¼Ϊ0
    for(int i=0;i<doublePanel.getGoldSets().size();i++){
        GoldData gold = DoublePanel.getGoldSets().get(i);
        doublePanel.updateGrade(gold);
    }
    int targetSum  = setting.getGrade();//��ȡĿ�����
    System.out.println("targetScore:"+targetSum);//Ŀ�������ӡ
    setting.setGrade(0);//��������ʼΪ0
    while (angle < 401)//�����תangle���Ƕ�
    {
        int rotate = angle;
        while(rotate>=0){//��ǰ��ת��rotate�Ƕ�������ѭ����
        	doublePanel.mining(1);//ѭ��һ�Σ�ƫתһ���Ƕȡ�
            rotate = rotate - 1;
        }
        doublePanel.calculate_k1_b1();//����ת����ǰ�Ƕ�Ϊangle��ʱ�򣬳�����
        while (doublePanel.isClawStretch_1()) {//clawStretch ��ʼΪtrue������ �����߽�����Ϊfalse�����ջ�
            //��ͣ���������ƶ��������ѵ����ӻ򵽴��½��߽������;
        	doublePanel.mining(1);//����һ�������ƶ��ķ�����
        }
        if(!doublePanel.isBackclaw_1()){
            assertTrue(doublePanel.isGrabSucceed_1());//ȷ����־λ�ı�
            if(doublePanel.getBeGrab_1()>=doublePanel.getGoldSets().size()||doublePanel.getBeGrab_1()<0){
                fail();
            }
            GoldData curGold=DoublePanel.getGoldSets().get(doublePanel.getBeGrab_1());//��ȡ��ǰץ�����ӵĶ���
            while(!doublePanel.isClawSpin_1()){//������ת״̬�������������ջ��ٴ�����ת״̬��
            	doublePanel.mining(1);//��ǰ����԰���ͨ������ض�����ı��ջ������ٶȵ�ѭ����
                if(doublePanel.isClawSpin_1()){
                    break;
                }//���ĳ�����Ž��������󵽴�߽��ջأ��򲻽��������жϣ���Ϊ�ջغ�״̬�����ͻ�䡣
                assertEquals(DoublePanel.getGoldSets().get(doublePanel.getBeGrab_1()).getGoldX()+25,(int)doublePanel.getClawX_1());
                assertEquals(DoublePanel.getGoldSets().get(doublePanel.getBeGrab_1()).getGoldY(),(int)doublePanel.getClawY_1());
                //�ȽϽ��ӵ��ƶ��Ƿ����Թ���Ϊ����ϵ�ƶ�
                if(curGold.getKind()==5) assertEquals(1,doublePanel.getStretchSpeed_1());
                else if(curGold.getGoldSize()>=100) assertEquals(2,doublePanel.getStretchSpeed_1());
                else if(curGold.getKind()==4) assertEquals(4,doublePanel.getStretchSpeed_1());
                else assertEquals(3,doublePanel.getStretchSpeed_1());
                //���ٶȸı�����ж�
            }
            oralSum = oralSum + setting.getGrade();//�ۼӽ�Ǯ
            setting.setGrade(0);//����ͨ�أ�ÿ�γ�����Ǯ����ʼ��Ϊ0��
        }
        doublePanel.restoration(1);//��λ
        angle = angle + 1;
    }
    System.out.println("finallyScore:"+oralSum);
    assertEquals(targetSum,oralSum);//�ж����н���ץ�����Ľ�Ǯ���Ƿ����㡣
}

@Test
public void testClawSpinChangeDirection() {
    for(int time = 20000; time >= 0; --time) {
        int beforeClawSpeed = doublePanel.getClawSpeed_1();
        assertTrue(doublePanel.isClawSpin_1());
        doublePanel.mining(1);
        if (doublePanel.getClawX_1() != 480.0D && doublePanel.getClawX_1()!= 680.0D) {
            assertEquals((long)beforeClawSpeed, (long)doublePanel.getClawSpeed_1());
        } else {
            assertEquals((long)(beforeClawSpeed * -1), (long)doublePanel.getClawSpeed_1());
        }
    }
}
}
