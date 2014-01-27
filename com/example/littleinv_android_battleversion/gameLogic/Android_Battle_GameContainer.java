package com.example.littleinv_android_battleversion.gameLogic;

import gameLogic.GameContainer;
import gameLogic.mode.PlayMode;
import gameLogic.mode.Result_ClearMode;
import gameLogic.mode.Result_GameOverMode;
import gameLogic.mode.StartMode;
import android.os.StrictMode;
import battleVersion.BattleFieldConfigBean;
import battleVersion.BattleMode.BattleMode;
import battleVersion.BattleResult_Lose.BattleResult_LoseMode;
import battleVersion.BattleResult_Win.BattleResult_WinMode;
import battleVersion.Battle_StartMode.Battle_StartMode;


/**
 * Android向け実装の、ゲームコンテナ
 * @author n-dolphin
 * @version 1.00 2014/01/17
 */
public class Android_Battle_GameContainer extends GameContainer{
	
	/**
	 * コンストラクタ
	 * @param Width 
	 * @param Height 
	 */
	public Android_Battle_GameContainer(Integer Width, Integer Height) {
		super(Width, Height, new Android_Battle_DrawImplement(Width, Height),new BattleFieldConfigBean());
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		
		super.addMode(new StartMode());
		super.addMode(new PlayMode());
		super.addMode(new Result_ClearMode());
		super.addMode(new Result_GameOverMode());
		
		
		super.addMode(new Battle_StartMode());
		super.addMode(new BattleMode());
		super.addMode(new BattleResult_WinMode());
		super.addMode(new BattleResult_LoseMode());
		

		super.changeMode(Battle_StartMode.name);
	}
	
	@Override
	public <Canvas> void  update(Canvas panel){
		super.update(panel);
	}

}
