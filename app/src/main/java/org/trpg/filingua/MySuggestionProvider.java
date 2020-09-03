package org.trpg.filingua;

import android.content.SearchRecentSuggestionsProvider;
//最近のクエリの基づく候補を表示するのに必要なコンテンツプロバイダ
//このクラスが実質的にすべての操作をデベロッパーに代わって行う。
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider(){
        setupSuggestions(AUTHORITY,MODE);
        //呼び出しでは検索権限の名前とデータベースのモードを渡す
    }

}
