package com.smartboxtv.movistartv.delgates;

import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.fragments.RecommendedFragment;

/**
 * Created by Esteban- on 19-04-14.
 */
public abstract class RecommendedDelegate {

    public abstract void like(Program p, RecommendedFragment fragment);
    public abstract void dislike(Program p, RecommendedFragment fragment);

}
