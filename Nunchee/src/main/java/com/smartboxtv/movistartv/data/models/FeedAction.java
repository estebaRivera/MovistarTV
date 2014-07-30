package com.smartboxtv.movistartv.data.models;

import com.smartboxtv.movistartv.services.DataMember;

/**
 * Created by Esteban- on 18-04-14.
 */
public class FeedAction {

    public String actionName;
    public boolean actionState;
    public String idAction;

    public FeedAction() {

    }
    @DataMember( member = "ActionName")
    public String getActionName() {
        return actionName;
    }

    @DataMember( member = "ActionName")
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @DataMember( member = "ActionState")
    public boolean isActionState() {
        return actionState;
    }

    @DataMember( member = "ActionState")
    public void setActionState(boolean actionState) {
        this.actionState = actionState;
    }

    @DataMember( member = "IdAction")
    public String getIdAction() {
        return idAction;
    }

    @DataMember( member = "IdAction")
    public void setIdAction(String idAction) {
        this.idAction = idAction;
    }
}
