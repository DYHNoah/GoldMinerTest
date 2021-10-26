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
* @since <pre>十月 16, 2021</pre>
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
 * 初始化金子和挖矿工的位子
*
*/
@Test
public void testInitData() throws Exception {
//TODO: 并没有测试我的位子
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
* 释放爪子
*/
@Test(timeout = 2000) @SuppressWarnings("all")
public void testReleaseClaw() throws Exception {
//TODO: Test goes here...
        int angle = 0;//angle表示旋转的角度
        DoublePanel.getGoldSets().clear();//清除金子
        while (angle <= 401) {//最大角度为401,保证可以遍历全部用例
            int rotate = angle;//当前循环的旋转角度次数
            while (rotate > 0) {//若rotate为0，则会出bug
                assertTrue(doublePanel.isClawSpin_1());//确保进入爪子旋转分支。
                doublePanel.mining(1);
                rotate = rotate - 1;
            }
            doublePanel.calculate_k1_b1();//当旋转到角度为angle的时候，出钩子
            if (Double.isInfinite(doublePanel.getK_1()) || Double.isNaN(doublePanel.getK_1())) {
                System.out.println("K:"+doublePanel.getK_1());
                fail();
            }
            while (!doublePanel.isBackclaw_1()) {//不停调用向下移动方法，直到到达下降边界后跳出;
                double beforeClawX = doublePanel.getClawX_1();//记录下降之前的X坐标。
                doublePanel.releaseClaw(1);//调用一次向下移动的方法。
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
            doublePanel.restoration(1);//复位
            angle = angle + 1;//下一次角度加一。
        }

}

/**
*
* Method: withdrawClaw()
* 收回爪子
*/
@Test(timeout = 1000)//限制执行时间为2s 存在两个bug 一个是withdrawclaw 两个if 另一个是死循环 收回条件不满足
public void testWithdrawClaw() throws Exception {
//TODO: Test goes here...
    int angle = 0;//angle表示旋转的角度
    DoublePanel.getGoldSets().clear();//清除金子
    while (angle <= 401) {//最大角度为401，遍历所有用例
        int rotate = angle;//当前循环的旋转角度次数
        while (rotate >= 0) {
            assertTrue(doublePanel.isClawSpin_1());//确保进入爪子旋转分支。
            doublePanel.mining(1);
            rotate = rotate - 1;
        }
        doublePanel.calculate_k1_b1();//当旋转到角度为angle的时候，出钩子
        while (!doublePanel.isBackclaw_1()) {//不停调用向下移动方法，直到到达下降边界后跳出;
        	doublePanel.mining(1);//调用一次向下移动的方法。
        }
        System.out.println("clawX0:" + doublePanel.getClawX0_1());
        System.out.println("clawY0:" + doublePanel.getClawY0_1());
        while (!doublePanel.isWithdrawSucceed(1)) {//测试钩子是否能回到初始位置，跳出。
            double beforeClawX = doublePanel.getClawX_1();//记录上升之前的X坐标。
            System.out.println("beforeclawX:" + doublePanel.getClawX_1());
            System.out.println("beforeclawY:" + doublePanel.getClawY_1());//打印向上移动之前的坐标
            doublePanel.withdrawClaw(1);//调用一次向上移动的方法。
            System.out.println("afterclawX:" + doublePanel.getClawX_1());
            System.out.println("afterclawY:" + doublePanel.getClawY_1());//打印向上移动之后的坐标
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
        //模拟爪子处于准备状态
        for(int number = time; number >= 0; --number) {
        	doublePanel.mining(1);
        }
        //获取出出爪时爪子的位置
        double x1 = doublePanel.getClawX_1();
        double y1 = doublePanel.getClawY_1();
        //模拟出爪子20ms
        doublePanel.calculate_k1_b1();
        doublePanel.mining(1);
        //获取20ms后爪子的位置
        double x2 = doublePanel.getClawX_1();
        double y2 = doublePanel.getClawY_1();
        //计算爪子的速度
        double speed = Math.sqrt((x2-x1)*(x2-x1) + (y1-y2)*(y1-y2));
        //判断是否与预期的速度相同，允许误差0
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
//    判定当前分数是否大于目标分，大于则通过
    for(int grade=setting.getTargetScore()-200;grade<setting.getTargetScore()+200;grade++){
        setting.setGrade(grade);
        if(setting.getGrade()>=setting.getTargetScore()){
            assertTrue(doublePanel.pass());
        }
    }
//    超时分数大于通过，分数小于不通过
    setting.setTime(60020);
    for(int grade=setting.getTargetScore()-200;grade<setting.getTargetScore()+200;grade++){
        setting.setGrade(grade);
        if(setting.getGrade()>=setting.getTargetScore()){
            assertTrue(doublePanel.pass());
        }else{
            assertFalse(doublePanel.pass());
        }
    }
//    判断没有金子也通过
//    goldSets = soloPanel.getGoldSets();
//    setting.setGrade(0);
//    setting.setTime(60020);
////    假设没有金子
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
//    前面60s都在模拟时间增长
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
//    边界测试
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
    int angle = 0;//angle表示旋转的角度
    int oralSum = 0;//初始化金子记录为0
    for(int i=0;i<doublePanel.getGoldSets().size();i++){
        GoldData gold = DoublePanel.getGoldSets().get(i);
        doublePanel.updateGrade(gold);
    }
    int targetSum  = setting.getGrade();//获取目标分数
    System.out.println("targetScore:"+targetSum);//目标分数打印
    setting.setGrade(0);//将分数初始为0
    while (angle < 401)//最多旋转angle个角度
    {
        int rotate = angle;
        while(rotate>=0){//当前旋转到rotate角度再跳出循环。
        	doublePanel.mining(1);//循环一次，偏转一个角度。
            rotate = rotate - 1;
        }
        doublePanel.calculate_k1_b1();//当旋转到当前角度为angle的时候，出钩子
        while (doublePanel.isClawStretch_1()) {//clawStretch 初始为true向下伸 碰见边界或金子为false向上收回
            //不停调用向下移动方法，搜到金子或到达下降边界后跳出;
        	doublePanel.mining(1);//调用一次向下移动的方法。
        }
        if(!doublePanel.isBackclaw_1()){
            assertTrue(doublePanel.isGrabSucceed_1());//确保标志位改变
            if(doublePanel.getBeGrab_1()>=doublePanel.getGoldSets().size()||doublePanel.getBeGrab_1()<0){
                fail();
            }
            GoldData curGold=DoublePanel.getGoldSets().get(doublePanel.getBeGrab_1());//获取当前抓到金子的对象
            while(!doublePanel.isClawSpin_1()){//处于旋转状态再跳出。（先收回再处于旋转状态）
            	doublePanel.mining(1);//若前面测试案例通过，则必定进入改变收回绳子速度的循环。
                if(doublePanel.isClawSpin_1()){
                    break;
                }//如果某次拉着金子上升后到达边界收回，则不进行下列判断，因为收回后状态会产生突变。
                assertEquals(DoublePanel.getGoldSets().get(doublePanel.getBeGrab_1()).getGoldX()+25,(int)doublePanel.getClawX_1());
                assertEquals(DoublePanel.getGoldSets().get(doublePanel.getBeGrab_1()).getGoldY(),(int)doublePanel.getClawY_1());
                //比较金子的移动是否是以钩子为参照系移动
                if(curGold.getKind()==5) assertEquals(1,doublePanel.getStretchSpeed_1());
                else if(curGold.getGoldSize()>=100) assertEquals(2,doublePanel.getStretchSpeed_1());
                else if(curGold.getKind()==4) assertEquals(4,doublePanel.getStretchSpeed_1());
                else assertEquals(3,doublePanel.getStretchSpeed_1());
                //对速度改变进行判断
            }
            oralSum = oralSum + setting.getGrade();//累加金钱
            setting.setGrade(0);//避免通关，每次出钩子钱都初始设为0；
        }
        doublePanel.restoration(1);//复位
        angle = angle + 1;
    }
    System.out.println("finallyScore:"+oralSum);
    assertEquals(targetSum,oralSum);//判断所有金子抓起来的金钱和是否满足。
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
