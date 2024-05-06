package com.example.overapp.Utils;

import android.util.Log;

import com.example.overapp.JSON.JsonPhrase;
import com.example.overapp.JSON.JsonSentence;
import com.example.overapp.JSON.JsonTran;
import com.example.overapp.JSON.JsonWord;
import com.example.overapp.database.Interpretation;
import com.example.overapp.database.Phrase;
import com.example.overapp.database.Sentence;
import com.example.overapp.database.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
//定义工具类，用于解析单词的JSON数据
public class GSON {


        // 采用Google的GSON开源框架
        public static Gson gson = new Gson();

        // 解析默认词库数据文件，并存放到数据库中
        public static void analysJSONandSAVE(String jsonContent) {
//            利用LitePal查看单词数据库中是否存在数据，存在则将相关数据全部清空
            if (!LitePal.findAll(Word.class).isEmpty()){
//                将与单词表联系的翻译，短语和句子一起清空
                LitePal.deleteAll(Interpretation.class);
                LitePal.deleteAll(Phrase.class);
                LitePal.deleteAll(Sentence.class);
            }
            // 解析的数据格式
//            定义列表存储解析后的JSON数据
            List<JsonSentence> jsonSentences = new ArrayList<>();
            List<JsonTran> jsonTrans = new ArrayList<>();
            List<JsonPhrase> jsonPhrases = new ArrayList<>();
//            转化为列表类型，gson.fromJson将JSON数据转化为Java对象，提供两个参数，分别是json字符串以及需要转换成对象的类型。
//List<Person> ps = gson.fromJson(str, new TypeToken<List<Person>>(){}.getType());转化成列表形式
//            可以看到上面的代码使用了TypeToken，它是gson提供的数据类型转换器，可以支持各种数据集合类型转换。
//            下同
            List<JsonWord> jsonWordList = gson.fromJson(jsonContent, new TypeToken<List<JsonWord>>() {}.getType());
//            遍历上述创建的列表，为每个JSON对象创建新的word对象，并设置相关信息，根据JSON数据的格式建立
//            现遍历但此部分
            for (JsonWord jsonWord : jsonWordList) {
                Word word = new Word();
                // 设置ID
                word.setWordId(jsonWord.getWordRank());
                // 设置单词
                word.setWord(jsonWord.getHeadWord());
                // 设置音标，检查英式发音是否为空，不为空
                if (jsonWord.getContent().getWord().getContent().getUkphone() != null) {
//                    如果json的音标中不包含分号，直接拼接设置属性
                    if (jsonWord.getContent().getWord().getContent().getUkphone().indexOf(";") == -1)
                        word.setUkPhone("[" + jsonWord.getContent().getWord().getContent().getUkphone() + "]");
                    else
//                        返至
                        word.setUkPhone("[" + jsonWord.getContent().getWord().getContent().getUkphone().split(";")[0] + "]");
                }
//                美音同上
                if (jsonWord.getContent().getWord().getContent().getUsphone() != null) {
                    if (jsonWord.getContent().getWord().getContent().getUsphone().indexOf(";") == -1)
                        word.setUsPhone("[" + jsonWord.getContent().getWord().getContent().getUsphone() + "]");
                    else
                        word.setUsPhone("[" + jsonWord.getContent().getWord().getContent().getUsphone().split(";")[0] + "]");
                }
                // 设置巧记
                if (jsonWord.getContent().getWord().getContent().getRemMethod() != null)
                    word.setRemMethod(jsonWord.getContent().getWord().getContent().getRemMethod().getVal());
                // 设置归属书目
                word.setBelongBook(jsonWord.getBookId());
                // 保存，litepal保存
                word.save();
                /*至此，单词的基本内容已经保存，接下来把其他表的数据保存并绑定到这个单词上*/
                // 设置短语
//                判断是否存在单词数据
                if (jsonWord.getContent().getWord().getContent().getPhrase() != null) {
//                   如果存在短语内容，则获取短语列表
                    jsonPhrases = jsonWord.getContent().getWord().getContent().getPhrase().getPhrases();
//                    遍历列表
                    for (JsonPhrase jsonPhrase : jsonPhrases) {
//                        都要创建对应的对象
                        Phrase phrase = new Phrase();
//                        设置与段与相关的数据，并保存
                        phrase.setChsPhrase(jsonPhrase.getpCn());
                        phrase.setEnPhrase(jsonPhrase.getpContent());
                        phrase.setWordId(jsonWord.getWordRank());
                        phrase.save();
                    }
                }
                // 设置释义
//                先获取jsonWord对象中的释义列表
                jsonTrans = jsonWord.getContent().getWord().getContent().getTrans();
                for (JsonTran jsonTran : jsonTrans) {
//                    为每个释义创建一个新的Interpretation对象
                    Interpretation interpretation = new Interpretation();
                    interpretation.setWordType(jsonTran.getPos());
                       // 设置Interpretation对象的中文释义（CHSMeaning）属性
                         // 因为JSON数据中中文释义存在符号问题先转化为中文
                    // 在设置之前，对中文释义进行了特殊处理：将连续的两个分号（"；；"）替换为一个分号（"；"），
                          // 将英文的逗号（","）替换为中文的逗号（"，"）
                    interpretation.setCHSMeaning(jsonTran.getTranCn().replace("；；", ";").replace(",","，"));
                    interpretation.setENMeaning(jsonTran.getTranOther());
                    interpretation.setWordId(jsonWord.getWordRank());
                    interpretation.save();
                }
                // 设置例句，同上操作，获取jsonWord对象中的释义列表，遍历设置信息保存
                if (jsonWord.getContent().getWord().getContent().getSentence() != null) {
                    jsonSentences = jsonWord.getContent().getWord().getContent().getSentence().getSentences();
                    for (JsonSentence jsonSentence : jsonSentences) {
                        Sentence sentence = new Sentence();
                        sentence.setChsSentence(jsonSentence.getsCn());
                        sentence.setEnSentence(jsonSentence.getsContent());
                        sentence.setWordId(jsonWord.getWordRank());
                        sentence.save();
                    }
                }
                // 清空数据，防止重复
                jsonPhrases.clear();
                jsonSentences.clear();
                jsonTrans.clear();
            }
            Log.d("GSON", "analyseDefaultAndSave: ");
        }

    }

