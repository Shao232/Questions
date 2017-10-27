package com.questions.http;

import com.questions.bean.QuestionsBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 11470 on 2017/10/23.
 */

public interface HttpApiManager {

    /**
     * subject 	int 	是 	选择考试科目类型，1：科目1；4：科目4
     * model 	string 	是 	驾照类型，可选择参数为：c1,c2,a1,a2,b1,b2；当subject=4时可省略
     * testType 	string 	否 	测试类型，rand：随机测试（随机100个题目），order：顺序测试（所选科目全部题目）
     * @return
     */
    @GET("jztk/query")
    Observable<ResultEntity<List<QuestionsBean>>> getData(@Query("subject")int subject,
                                                          @Query("model")String model,@Query("testType")String testType,
                                                          @Query("key")String keyValue);
}
