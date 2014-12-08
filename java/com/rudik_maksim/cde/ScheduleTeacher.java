package com.rudik_maksim.cde;

import android.util.Log;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Максим on 13.11.2014.
 */
public class ScheduleTeacher {
    ArrayList<String> day_week      = new ArrayList<String>();
    ArrayList<String> week_type     = new ArrayList<String>();
    ArrayList<String> time          = new ArrayList<String>();
    ArrayList<String> room          = new ArrayList<String>();
    ArrayList<String> place         = new ArrayList<String>();
    ArrayList<String> title_subject = new ArrayList<String>();
    ArrayList<String> person_title  = new ArrayList<String>();
    ArrayList<String> status        = new ArrayList<String>();
    ArrayList<String> gr            = new ArrayList<String>();

    private HashMap<String, String> teacherIdHashMap = new HashMap<String, String>();

    public ScheduleTeacher(){
        // A
        teacherIdHashMap.put("Абдуллаева Любовь Магомедовна", "165527");
        teacherIdHashMap.put("Алексеев Геннадий Валентинович", "165646");
        teacherIdHashMap.put("Алиев Тауфик Измайлович", "100040");
        teacherIdHashMap.put("Андреев Лев Николаевич", "100889");
        teacherIdHashMap.put("Арет Вальдур Аулисович", "165023");
        teacherIdHashMap.put("Арустамов Сергей Аркадьевич", "147308");
        teacherIdHashMap.put("Артемова Галина Олеговна", "126205");
        // Б
        teacherIdHashMap.put("Багдасарова Ольга Васильевна", "100892");
        teacherIdHashMap.put("Балошин Юрий Александрович", "100536");
        teacherIdHashMap.put("Балюбаш Виктор Александрович", "165287");
        teacherIdHashMap.put("Баранов Александр Васильевич", "132498");
        teacherIdHashMap.put("Бахолдин Алексей Валентинович", "110941");
        teacherIdHashMap.put("Башнина Галина Львовна", "100650");
        teacherIdHashMap.put("Бегунов Александр Андреевич", "165033");
        teacherIdHashMap.put("Белов Павел Александрович", "111680");
        teacherIdHashMap.put("Бобцов Алексей Алексеевич", "104990");
        teacherIdHashMap.put("Богатырев Владимир Анатольевич", "127286");
        teacherIdHashMap.put("Богданова Елена Леонардовна", "144173");
        teacherIdHashMap.put("Болтунов Геннадий Иванович", "100035");
        teacherIdHashMap.put("Борзенко Евгений Иванович", "165082");
        teacherIdHashMap.put("Борисов Олег Сергеевич", "105846");
        teacherIdHashMap.put("Бройко Юлия Владимировна", "165847");
        teacherIdHashMap.put("Бугров Владислав Евгеньевич", "168462");
        teacherIdHashMap.put("Будько Марина Борисовна", "118075");
        teacherIdHashMap.put("Булат Лев Петрович", "165751");
        teacherIdHashMap.put("Буткарёв Алексей Георгиевич", "165089");
        teacherIdHashMap.put("Бухановский Александр Валерьевич", "139848");
        teacherIdHashMap.put("Бушуев Александр Борисович", "100004");
        teacherIdHashMap.put("Боярский Кирилл Кириллович", "100537");
        // В
        teacherIdHashMap.put("Валетов Вячеслав Алексеевич", "100705");
        teacherIdHashMap.put("Варламов Борис Александрович", "113097");
        teacherIdHashMap.put("Вартанян Тигран Арменакович", "132500");
        teacherIdHashMap.put("Василенок Виктор Леонидович", "165094");
        teacherIdHashMap.put("Васюхин Олег Валентинович", "100971");
        teacherIdHashMap.put("Виноградова Анна Вячеславовна", "159635");
        teacherIdHashMap.put("Вознесенская Анна Олеговна", "113365");
        teacherIdHashMap.put("Волков Дмитрий Павлович", "100489");
        teacherIdHashMap.put("Волковский Сергей Александрович", "138424");
        // Г
        teacherIdHashMap.put("Галайдин Павел Андреевич", "105888");
        teacherIdHashMap.put("Голубев Андрей Александрович", "100973");
        teacherIdHashMap.put("Голубок Александр Олегович", "105083");
        teacherIdHashMap.put("Горлушкина Наталия Николаевна", "103466");
        teacherIdHashMap.put("Григорьев Александр Юрьевич", "165337");
        teacherIdHashMap.put("Григорьев Валерий Владимирович", "100007");
        teacherIdHashMap.put("Григорьев Владимир Александрович", "159663");
        teacherIdHashMap.put("Грудинин Владимир Алексеевич", "169375");
        teacherIdHashMap.put("Гуров Игорь Петрович", "102234");
        teacherIdHashMap.put("Гусарова Наталия Федоровна", "103489");
        teacherIdHashMap.put("Гончаров Александр Сергеевич", "133877");
        // Д
        teacherIdHashMap.put("Демин Анатолий Владимирович", "100865");
        teacherIdHashMap.put("Денисюк Игорь Юрьевич", "127254");
        teacherIdHashMap.put("Дмитриев Александр Леонидович", "101827");
        teacherIdHashMap.put("Добряков Владимир Александрович", "165493");
        // E
        teacherIdHashMap.put("Евдокимов Александр Александрович", "165385");
        teacherIdHashMap.put("Елизаров Роман Анатольевич", "111723");
        teacherIdHashMap.put("Емельянцев Геннадий Иванович", "106190");
        teacherIdHashMap.put("Ермолаев Валерий Леонидович", "105924");
        // Ж
        teacherIdHashMap.put("Жаринов Игорь Олегович", "131926");
        teacherIdHashMap.put("Жигулин Георгий Петрович", "102587");
        // З
        teacherIdHashMap.put("Забодалова Людмила Александровна", "165052");
        teacherIdHashMap.put("Заричняк Юрий Петрович", "100492");
        teacherIdHashMap.put("Зикратов Игорь Алексеевич", "152414");
        teacherIdHashMap.put("Зленко Андрей Николаевич", "134011");
        teacherIdHashMap.put("Золотарев Владимир Михайлович", "100458");
        teacherIdHashMap.put("Зудилова Татьяна Викторовна", "143501");
        teacherIdHashMap.put("Зинчик Александр Адольфович", "105840");
        teacherIdHashMap.put("Зингеренко Юрий Александрович", "105982");
        teacherIdHashMap.put("Зубок Дмитрий Александрович", "104861");
        // И
        teacherIdHashMap.put("Иванов Владимир Леонидович", "175687");
        teacherIdHashMap.put("Иголкин Алексей Федорович", "165307");
        teacherIdHashMap.put("Ишанин Геннадий Григорьевич", "100331");
        teacherIdHashMap.put("Ишевский Александр Леонидович", "165314");
        teacherIdHashMap.put("Иванова Наталия Юрьевна", "100186");
        teacherIdHashMap.put("Иванова Татьяна Владимировна", "105200");
        teacherIdHashMap.put("Иванов Владимир Петрович", "105891");
        teacherIdHashMap.put("Иванов Александр Николаевич", "117253");
        teacherIdHashMap.put("Иванов Роман Владимирович", "118758");
        teacherIdHashMap.put("Иванов Андрей Юрьевич", "125462");
        teacherIdHashMap.put("Иванов Сергей Евгеньевич", "130650");
        teacherIdHashMap.put("Иванов Сергей Владимирович", "139947");
        teacherIdHashMap.put("Иванов Андрей Витальевич", "140466");
        teacherIdHashMap.put("Иванов Евгений Юрьевич", "142159");
        teacherIdHashMap.put("Иванов Дмитрий Николаевич", "149705"); // also other teacher 165417
        teacherIdHashMap.put("Ивановская Людмила Сергеевна", "165302");
        teacherIdHashMap.put("Иванов Владислав Иванович", "165416");
        teacherIdHashMap.put("Иванова Марина Александровна", "165550");
        teacherIdHashMap.put("Иванов Владимир Леонидович", "175687");
        teacherIdHashMap.put("Ищенко Алексей Петрович", "105346");
        teacherIdHashMap.put("Йылмаз Ольга Александровна", "118391");
        // K
        teacherIdHashMap.put("Камоцкий Владимир Ильич", "165315");
        teacherIdHashMap.put("Каракулев Юрий Александрович", "100867");
        teacherIdHashMap.put("Карасев Вячеслав Борисович", "100413");
        teacherIdHashMap.put("Кармановский Николай Сергеевич", "100187");
        teacherIdHashMap.put("Карпова Галина Васильевна", "100914");
        teacherIdHashMap.put("Кириллов Вадим Васильевич", "165744");
        teacherIdHashMap.put("Кирилловский Владимир Константинович", "100897");
        teacherIdHashMap.put("Киселев Сергей Степанович", "104980");
        teacherIdHashMap.put("Кисс Валерий Вячеславович", "165114");
        teacherIdHashMap.put("Коваленко Анатолий Николаевич", "105889");
        teacherIdHashMap.put("Козлов Сергей Аркадьевич", "100656");
        teacherIdHashMap.put("Колесникова Тамара Дмитриевна", "102164");
        teacherIdHashMap.put("Конопелько Леонид Алексеевич", "104098");
        teacherIdHashMap.put("Коняхин Игорь Алексеевич", "100354");
        teacherIdHashMap.put("Кораблев Алексей Владимирович", "185329");
        teacherIdHashMap.put("Кораблев Владимир Антонович", "106191");
        teacherIdHashMap.put("Кораблева Ольга Николаевна", "154289");
        teacherIdHashMap.put("Корешев Сергей Николаевич", "123269");
        teacherIdHashMap.put("Корнеев Георгий Александрович", "105590");
        teacherIdHashMap.put("Королев Александр Александрович", "100660");
        teacherIdHashMap.put("Коротаев Валерий Викторович", "100333");
        teacherIdHashMap.put("Костишин Максим Олегович", "140873");
        teacherIdHashMap.put("Котельников Юрий Петрович", "100010");
        teacherIdHashMap.put("Красавцев Валерий Михайлович", "100459");
        teacherIdHashMap.put("Кузьмина Ольга Викторовна", "101024");
        teacherIdHashMap.put("Кулагин Вячеслав Сергеевич", "100383");
        teacherIdHashMap.put("Куликов Дмитрий Дмитриевич", "100716");
        teacherIdHashMap.put("Куркин Андрей Владимирович", "119319");
        teacherIdHashMap.put("Кустарев Валерий Павлович", "100974");
        teacherIdHashMap.put("Кустикова Марина Александровна", "106049");
        teacherIdHashMap.put("Кубенский Александр Александрович", "125566");
        teacherIdHashMap.put("Кубенский Михаил Николаевич", "135230");
        teacherIdHashMap.put("Куприянов Дмитрий Владимирович", "148981");
        teacherIdHashMap.put("Курашова Светлана Александровна", "102970");
        teacherIdHashMap.put("Круглов Алексей Александрович", "165403");
        teacherIdHashMap.put("Ключев Аркадий Олегович", "104008");
        // Л
        teacherIdHashMap.put("Лаврищев Илья Борисович", "165395");
        teacherIdHashMap.put("Лазарев Виктор Лазаревич", "165526");
        teacherIdHashMap.put("Лапин Иван Александрович", "100622");
        teacherIdHashMap.put("Латыев Святослав Михайлович", "100941");
        teacherIdHashMap.put("Лебедько Евгений Георгиевич", "100870");
        teacherIdHashMap.put("Лисицына Любовь Сергеевна", "106016");
        teacherIdHashMap.put("Лукьянов Геннадий Николаевич", "100494");
        teacherIdHashMap.put("Лямин Андрей Владимирович", "104080");
        teacherIdHashMap.put("Лапшина Людмила Павловна", "101648");
        teacherIdHashMap.put("Лукина Марина Владимировна", "132313");
        // М
        teacherIdHashMap.put("Магдиев Ринат Рауфович", "130661");
        teacherIdHashMap.put("Макарченко Марина Арнольдовна", "165264");
        teacherIdHashMap.put("Малышев Александр Александрович", "165274");
        teacherIdHashMap.put("Мальцева Надежда Константиновна", "100337");
        teacherIdHashMap.put("Мамченко Валерий Олегович", "165278");
        teacherIdHashMap.put("Маркитанова Лидия Ивановна", "165528");
        teacherIdHashMap.put("Маркушевская Лариса Петровна", "100253");
        teacherIdHashMap.put("Марусина Мария Яковлевна", "105516");
        teacherIdHashMap.put("Матвеев Юрий Николаевич", "102365");
        teacherIdHashMap.put("Медунецкий Виктор Михайлович", "127602");
        teacherIdHashMap.put("Меледина Татьяна Викторовна", "165160");
        teacherIdHashMap.put("Мельников Виталий Геннадьевич", "105835");
        teacherIdHashMap.put("Мельников Геннадий Иванович", "100164");
        teacherIdHashMap.put("Мешковский Игорь Касьянович", "100546");
        teacherIdHashMap.put("Мирошниченко Георгий Петрович", "100539");
        teacherIdHashMap.put("Митрофанов Андрей Сергеевич", "100415");
        teacherIdHashMap.put("Михайлова Ирина Анатольевна", "127269");
        teacherIdHashMap.put("Мурашова Светлана Витальевна", "154628");
        teacherIdHashMap.put("Мусалимов Виктор Михайлович", "100141");
        teacherIdHashMap.put("Муханин Лев Григорьевич", "100098");
        teacherIdHashMap.put("Милованович Екатерина Воиславовна", "106026");
        teacherIdHashMap.put("Маятин Александр Владимирович", "114568");
        // Н
        teacherIdHashMap.put("Назарова Виктория Владимировна", "165179");
        teacherIdHashMap.put("Немилов Сергей Владимирович", "105943");
        teacherIdHashMap.put("Неронов Юрий Ильич", "105093");
        teacherIdHashMap.put("Нечаев Владимир Анатольевич", "179347");
        teacherIdHashMap.put("Никоноров Николай Валентинович", "105942");
        teacherIdHashMap.put("Норин Александр Владимирович", "103901");
        teacherIdHashMap.put("Носова Ирина Леонидовна", "177882");
        teacherIdHashMap.put("Никитина Мария Владимировна", "111798");
        teacherIdHashMap.put("Набока Оксана Анатольевна", "151194");
        // О
        teacherIdHashMap.put("Орлова Анна Олеговна", "104397");
        teacherIdHashMap.put("Овсянникова Татьяна Николаевна", "130606");
        teacherIdHashMap.put("Ольконе Владимир Орестович", "136368");
        // П
        teacherIdHashMap.put("Падун Борис Степанович", "100721");
        teacherIdHashMap.put("Панкратова Татьяна Федоровна", "100625");
        teacherIdHashMap.put("Парамонов Павел Павлович", "104576");
        teacherIdHashMap.put("Переходко Федор Георгиевич", "144142");
        teacherIdHashMap.put("Петрашень Александр Георгиевич", "100627");
        teacherIdHashMap.put("Пешехонов Владимир Григорьевич", "101714");
        teacherIdHashMap.put("Пешков Андрей Иванович", "146624");
        teacherIdHashMap.put("Пилипенко Николай Васильевич", "100495");
        teacherIdHashMap.put("Платунов Алексей Евгеньевич", "100053");
        teacherIdHashMap.put("Подлесных Виктор Иванович", "100981");
        teacherIdHashMap.put("Полатайко Сергей Васильевич", "165960");
        teacherIdHashMap.put("Попов Игорь Юрьевич", "100628");
        teacherIdHashMap.put("Прокопенко Виктор Трофимович", "101396");
        teacherIdHashMap.put("Прокопчук Сергей Сафронович", "141748");
        teacherIdHashMap.put("Пронин Владимир Александрович", "165542");
        teacherIdHashMap.put("Процуто Марина Владимировна", "154016");
        teacherIdHashMap.put("Повышев Владислав Вячеславович", "113319");
        teacherIdHashMap.put("Пашин Валерий Федорович", "100547");
        teacherIdHashMap.put("Панкратьев Олег Владимирович", "136189");
        // P
        teacherIdHashMap.put("Рахманов Юрий Алексеевич", "165445");
        teacherIdHashMap.put("Резников Станислав Сергеевич", "116826");
        teacherIdHashMap.put("Розанов Николай Николаевич", "105965");
        teacherIdHashMap.put("Романов Алексей Евгеньевич", "168460");
        teacherIdHashMap.put("Рыков Владимир Алексеевич", "165627");
        teacherIdHashMap.put("Рынская Алла Константиновна", "138970");
        // C
        teacherIdHashMap.put("Салихов Владимир Иванович", "179302");
        teacherIdHashMap.put("Светлов Дмитрий Анатольевич", "117817");
        teacherIdHashMap.put("Свечникова Наталья Олеговна", "103551");
        teacherIdHashMap.put("Сергеева Ирина Григорьевна", "165444");
        teacherIdHashMap.put("Сергиенко Ольга Ивановна", "158710");
        teacherIdHashMap.put("Серебров Анатолий Иринархович", "100325");
        teacherIdHashMap.put("Серебряков Виктор Анатольевич", "105932");
        teacherIdHashMap.put("Сизиков Валерий Сергеевич", "101545");
        teacherIdHashMap.put("Смирнов Алексей Владимирович", "150723");
        teacherIdHashMap.put("Смирнов Вячеслав Павлович", "100541");
        teacherIdHashMap.put("Смирнов Сергей Борисович", "104176");
        teacherIdHashMap.put("Смолин Артем Александрович", "163250");
        teacherIdHashMap.put("Станкевич Андрей Сергеевич", "116501");
        teacherIdHashMap.put("Стафеев Сергей Константинович", "100670");
        teacherIdHashMap.put("Степанов Олег Андреевич", "106021");
        teacherIdHashMap.put("Сударчиков Сергей Алексеевич", "105822");
        teacherIdHashMap.put("Сухорукова Марина Вилевна", "101403");
        teacherIdHashMap.put("Субботин Дмитрий Андреевич", "134586");
        teacherIdHashMap.put("Силаева Кира Валерьевна", "175268");
        teacherIdHashMap.put("Стригалев Владимир Евгеньевич", "138974");
        // T
        teacherIdHashMap.put("Тарлыков Владимир Алексеевич", "100417");
        teacherIdHashMap.put("Тертычный Владимир Юрьевич", "100639");
        teacherIdHashMap.put("Тимофеев Борис Павлович", "100146");
        teacherIdHashMap.put("Ткалич Вера Леонидовна", "105824");
        teacherIdHashMap.put("Толстикова Ирина Ивановна", "101049");
        teacherIdHashMap.put("Томасов Валентин Сергеевич", "100575");
        teacherIdHashMap.put("Точилина Татьяна Вячеславовна", "125220");
        teacherIdHashMap.put("Тропченко Александр Ювенальевич", "103435");
        // У
        teacherIdHashMap.put("Удин Евгений Геннадьевич", "103541");
        teacherIdHashMap.put("Успенская Майя Валерьевна", "104332");
        teacherIdHashMap.put("Ушаков Анатолий Владимирович", "100018");
        // Ф
        teacherIdHashMap.put("Федоров Анатолий Валентинович", "132497");
        teacherIdHashMap.put("Федоров Борис Александрович", "104982");
        teacherIdHashMap.put("Филичева Надежда Викторовна", "102355");
        // Х
        teacherIdHashMap.put("Храмов Валерий Юрьевич", "100418");
        teacherIdHashMap.put("Хлопотов Максим Валерьевич", "115801");
        // Ц
        teacherIdHashMap.put("Цветков Олег Борисович", "125427");
        teacherIdHashMap.put("Цуканова Галина Ивановна", "100901");
        teacherIdHashMap.put("Цуканова Ольга Анатольевна", "115138");
        // Ч
        teacherIdHashMap.put("Чиков Константин Никитич", "100487");
        teacherIdHashMap.put("Чугунов Андрей Владимирович", "151216");
        // Ш
        teacherIdHashMap.put("Шалыто Анатолий Абрамович", "105061");
        teacherIdHashMap.put("Шандыбина Галина Дмитриевна", "101562");
        teacherIdHashMap.put("Шапиро Наталья Александровна", "165503");
        teacherIdHashMap.put("Шарков Александр Васильевич", "100496");
        teacherIdHashMap.put("Шипилов Павел Александрович", "100283");
        teacherIdHashMap.put("Шлейкин Александр Герасимович", "165509");
        teacherIdHashMap.put("Шукалов Анатолий Владимирович", "188341");
        teacherIdHashMap.put("Шарков Александр Васильевич", "100496");
        teacherIdHashMap.put("Шарков Илья Александрович", "121239");
        // Щ
        teacherIdHashMap.put("Щеглов Андрей Юрьевич", "102953");
        teacherIdHashMap.put("Щеголев Валерий Александрович", "106071");
        // Я
        teacherIdHashMap.put("Яблочников Евгений Иванович", "104754");
        teacherIdHashMap.put("Яковлев Евгений Борисович", "105133");
        teacherIdHashMap.put("Яськов Андрей Дмитриевич", "101398");
    }

    public String getTeacherId(String fio){
        if (teacherIdHashMap.containsKey(fio)){
            return teacherIdHashMap.get(fio);
        }else
            return "";
    }

    public ArrayList<String> getSimilarTeachers(String s){
        ArrayList<String> similar = new ArrayList<String>();
        s = s.toLowerCase();

        for (String key: teacherIdHashMap.keySet()){
            String lowerKey = key.toLowerCase();

            if (lowerKey.contains(s))
                similar.add(key);
        }

        return similar;
    }

    public ArrayList<String> getAllTeachers(){
        ArrayList<String> keys = new ArrayList<String>();

        for (String key: teacherIdHashMap.keySet()){
            keys.add(key);
        }

        return keys;
    }

    public int getCountRecords(){
        return teacherIdHashMap.size();
    }


    public void parse(String pid){
        String response = executePost("http://www.ifmo.ru/mobile/schedule_pid_gr.php", "login=ifmo01&pass=01ifmo04&pid=" + pid);

        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                day_week.add(jsonObject.get("day_week").toString());
                week_type.add(jsonObject.get("week_type").toString());
                time.add(jsonObject.get("time").toString());
                room.add(jsonObject.get("room").toString());
                place.add(jsonObject.get("place").toString());
                title_subject.add(jsonObject.get("title_subject").toString());
                person_title.add(jsonObject.get("person_title").toString());
                status.add(jsonObject.get("status").toString());
                gr.add(jsonObject.get("gr").toString());
            }

            Global.CDEData.ST_DATA.add(day_week);
            Global.CDEData.ST_DATA.add(week_type);
            Global.CDEData.ST_DATA.add(time);
            Global.CDEData.ST_DATA.add(room);
            Global.CDEData.ST_DATA.add(place);
            Global.CDEData.ST_DATA.add(title_subject);
            Global.CDEData.ST_DATA.add(person_title);
            Global.CDEData.ST_DATA.add(status);
            Global.CDEData.ST_DATA.add(gr);

        }catch (Exception ex){
            Log.d(Global.Debug.LOG_TAG, "error parse scheduleNew: " + ex.toString());
        };
    }

    public String executePost(String targetURL, String urlParameters){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
