package com.easyget.terminal.base.util;

import java.util.ArrayList;
import java.util.List;

import com.easyget.terminal.base.view.control.InputType;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;


/**
 *
 * @author 李丹
 * 创建时间：2014年12月12日 上午11:28:29
 * @version 1.3
 *
 */
public class CursorUtils {

	/**
	 * 将光标移动到下一个输入框
	 * @param parent
	 */
	public static void moveFocusToNextText(Parent parent){
		List<TextField> textList = findAllTextField(parent);
		for(TextField text:textList){
			if(text.isFocused()){
				TextField nextText = findNextTextField(parent,text);
				if(null!=nextText){
					if(nextText instanceof InputType){
						InputType itext = (InputType)nextText;
						if(itext.getType() == InputType.AUTO_MIX || 
								itext.getType() == InputType.AUTO_NUMBER){
							nextText.requestFocus();
						}
					}else{
						nextText.requestFocus();
					}
				}
			}
		}
	}
	
	/**
	 * 获取下一个输入框
	 * @param root
	 * @param textNode
	 * @return
	 */
	private static TextField findNextTextField(Parent root,TextField textNode){
		List<TextField> textFieldList = findAllTextField(root);
		int index = textFieldList.indexOf(textNode);
		if(index>-1&&index!=(textFieldList.size()-1)){
			return textFieldList.get(index+1);
		}else{
			return null;
		}
		
	}
	
	private static List<TextField> findAllTextField(Parent root){
		List<TextField> textFieldList = new ArrayList<TextField>();
		ObservableList<Node> nodes = root
				.getChildrenUnmodifiable();
		for (Node node : nodes) {
			if (node instanceof TextField) {
				textFieldList.add((TextField)node);
			}else if(node instanceof Parent){
				List<TextField> childList = findAllTextField((Parent)node);
				textFieldList.addAll(childList);
			}
		}
		return textFieldList;
	}

}
