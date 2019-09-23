package kr.co.itcen.mysite.action.board;

import kr.co.itcen.web.mvc.Action;
import kr.co.itcen.web.mvc.ActionFactory;

public class BoardActionFactory extends ActionFactory {

	@Override
	public Action getAction(String actionName) {
		Action action = null;

		if("view".equals(actionName)) {
			action = new ViewAction();
		} else if("write".equals(actionName)) {
			action = new WriteAction(); 
		} else if("writeform".equals(actionName)) {
			action = new WriteformAction(); 
		}else {
			action = new ListAction();
		}
		return action;
	}

}
