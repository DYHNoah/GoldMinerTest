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
* @since <pre>十月 16, 2021</pre>
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
        SoloPanel.getGoldSets().clear();//清除金子
        while (angle <= 401) {//最大角度为401,保证可以遍历全部用例
            int rotate = angle;//当前循环的旋转角度次数
            while (rotate >0) {//若rotate为0，则会出bug
                assertTrue(doublePanel.isClawSpin());//确保进入爪子旋转分支。
                doublePanel.mining();
                rotate = rotate - 1;
            }
            doublePanel.calculate_k_b();//当旋转到角度为angle的时候，出钩子
            if (Double.isInfinite(doublePanel.getK()) || Double.isNaN(soloPanel.getK())) {
                System.out.println("K:"+doublePanel.getK());
                fail();
            }
            while (!soloPanel.isBackclaw()) {//不停调用向下移动方法，直到到达下降边界后跳出;
                double beforeClawX = soloPanel.getClawX();//记录下降之前的X坐标。
                soloPanel.releaseClaw();//调用一次向下移动的方法。
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
            soloPanel.restoration();//复位
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
    SoloPanel.getGoldSets().clear();//清除金子
    while (angle <= 401) {//最大角度为401，遍历所有用例
        int rotate = angle;//当前循环的旋转角度次数
        while (rotate >= 0) {
            assertTrue(soloPanel.isClawSpin());//确保进入爪子旋转分支。
            soloPanel.mining();
            rotate = rotate - 1;
        }
        soloPanel.calculate_k_b();//当旋转到角度为angle的时候，出钩子
        while (!soloPanel.isBackclaw()) {//不停调用向下移动方法，直到到达下降边界后跳出;
            soloPanel.mining();//调用一次向下移动的方法。
        }
        System.out.println("clawX0:" + soloPanel.getClawX0());
        System.out.println("clawY0:" + soloPanel.getClawY0());
        while (!soloPanel.isWithdrawSucceed()) {//测试钩子是否能回到初始位置，跳出。
            double beforeClawX = soloPanel.getClawX();//记录上升之前的X坐标。
            System.out.println("beforeclawX:" + soloPanel.getClawX());
            System.out.println("beforeclawY:" + soloPanel.getClawY());//打印向上移动之前的坐标
            soloPanel.withdrawClaw();//调用一次向上移动的方法。
            System.out.println("afterclawX:" + soloPanel.getClawX());
            System.out.println("afterclawY:" + soloPanel.getClawY());//打印向上移动之后的坐标
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
        //模拟爪子处于准备状态
        for(int number = time; number >= 0; --number) {
            soloPanel.mining();
        }
        //获取出出爪时爪子的位置
        double x1 = soloPanel.getClawX();
        double y1 = soloPanel.getClawY();
        //模拟出爪子20ms
        soloPanel.calculate_k_b();
        soloPanel.mining();
        //获取20ms后爪子的位置
        double x2 = soloPanel.getClawX();
        double y2 = soloPanel.getClawY();
        //计算爪子的速度
        double speed = Math.sqrt((x2-x1)*(x2-x1) + (y1-y2)*(y1-y2));
        //判断是否与预期的速度相同，允许误差0
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
//    判定当前分数是否大于目标分，大于则通过
    for(int grade=setting.getTargetScore()-200;grade<setting.getTargetScore()+200;grade++){
        setting.setGrade(grade);
        if(setting.getGrade()>=setting.getTargetScore()){
            assertTrue(soloPanel.pass());
        }
    }
//    超时分数大于通过，分数小于不通过
    setting.setTime(60020);
    for(int grade=setting.getTargetScore()-200;grade<setting.getTargetScore()+200;grade++){
        setting.setGrade(grade);
        if(setting.getGrade()>=setting.getTargetScore()){
            assertTrue(soloPanel.pass());
        }else{
            assertFalse(soloPanel.pass());
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
//    边界测试
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
    int angle = 0;//angle表示旋转的角度
    int oralSum = 0;//初始化金子记录为0
    for(int i=0;i<soloPanel.getGoldSets().size();i++){
        GoldData gold = SoloPanel.getGoldSets().get(i);
        soloPanel.updateGrade(gold);
    }
    int targetSum  = setting.getGrade();//获取目标分数
    System.out.println("targetScore:"+targetSum);//目标分数打印
    setting.setGrade(0);//将分数初始为0
    while (angle < 401)//最多旋转angle个角度
    {
        int rotate = angle;
        while(rotate>=0){//当前旋转到rotate角度再跳出循环。
            soloPanel.mining();//循环一次，偏转一个角度。
            rotate = rotate - 1;
        }
        soloPanel.calculate_k_b();//当旋转到当前角度为angle的时候，出钩子
        while (soloPanel.isClawStretch()) {//clawStretch 初始为true向下伸 碰见边界或金子为false向上收回
            //不停调用向下移动方法，搜到金子或到达下降边界后跳出;
            soloPanel.mining();//调用一次向下移动的方法。
        }
        if(!soloPanel.isBackclaw()){
            assertTrue(soloPanel.isGrabSucceed());//确保标志位改变
            if(soloPanel.getBeGrab()>=soloPanel.getGoldSets().size()||soloPanel.getBeGrab()<0){
                fail();
            }
            GoldData curGold=SoloPanel.getGoldSets().get(soloPanel.getBeGrab());//获取当前抓到金子的对象
            while(!soloPanel.isClawSpin()){//处于旋转状态再跳出。（先收回再处于旋转状态）
                soloPanel.mining();//若前面测试案例通过，则必定进入改变收回绳子速度的循环。
                if(soloPanel.isClawSpin()){
                    break;
                }//如果某次拉着金子上升后到达边界收回，则不进行下列判断，因为收回后状态会产生突变。
                assertEquals(SoloPanel.getGoldSets().get(soloPanel.getBeGrab()).getGoldX()+25,(int)soloPanel.getClawX());
                assertEquals(SoloPanel.getGoldSets().get(soloPanel.getBeGrab()).getGoldY(),(int)soloPanel.getClawY());
                //比较金子的移动是否是以钩子为参照系移动
                if(curGold.getKind()==5) assertEquals(1,soloPanel.getStretchSpeed());
                else if(curGold.getGoldSize()>=100) assertEquals(2,soloPanel.getStretchSpeed());
                else if(curGold.getKind()==4) assertEquals(4,soloPanel.getStretchSpeed());
                else assertEquals(3,soloPanel.getStretchSpeed());
                //对速度改变进行判断
            }
            oralSum = oralSum + setting.getGrade();//累加金钱
            setting.setGrade(0);//避免通关，每次出钩子钱都初始设为0；
        }
        soloPanel.restoration();//复位
        angle = angle + 1;
    }
    System.out.println("finallyScore:"+oralSum);
    assertEquals(targetSum,oralSum);//判断所有金子抓起来的金钱和是否满足。
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
