package pt.iscte.row_timer.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.row_timer.android.model.Alignment;
import pt.iscte.row_timer.android.model.BoatType;
import pt.iscte.row_timer.android.model.Category;
import pt.iscte.row_timer.android.model.Competitor;
import pt.iscte.row_timer.android.model.Crew;
import pt.iscte.row_timer.android.model.CrewMember;
import pt.iscte.row_timer.android.model.Person;
import pt.iscte.row_timer.android.model.Race;
import pt.iscte.row_timer.android.model.RowingEvent;

/**
 * Class that handles local (adnroid) database interaction
 */
public class RowingEventsDataSource {
    private static final String TAG = "RowingEventsDataSource";
    protected SQLiteDatabase db;
    protected RowingEventsDatabaseHelper dbhelper;

    public RowingEventsDataSource (Context c) {
        dbhelper = new RowingEventsDatabaseHelper(c);
    }

    public void open() {
        db = dbhelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }


    // Synchronization logic

    /**
     * Synchronize a full event (received by REST) into local database
     *
     * @param rowingEvent
     */
    public void synchronizeEvent(RowingEvent rowingEvent) {
        Log.d(TAG,"synchronizeEvent()");
        open();
        RowingEvent existingRowingEvent = readRowingEvent(rowingEvent.getId());
        deleteRowingEventTree(existingRowingEvent);
        createRowingEventTree(rowingEvent);
        close();
    }

    /**
     * Create the full rowing event tree in all tables
     * @param rowingEvent
     */
    public void createRowingEventTree(RowingEvent rowingEvent) {
        Log.d(TAG,"createRowingEventTree()");
        createRowingEvent(rowingEvent);
        List<Race> races = rowingEvent.getEventRaces();
        for (Race race : races) {
            createRaceTree(race);
        }
    }

    /**
     * Delete all tree of objects in the tables
     */
    public void deleteRowingEventTree(RowingEvent rowingEvent) {
        Log.d(TAG,"deleteRowingEventTree()");
        List<Race> races = rowingEvent.getEventRaces();
        if ( races != null ) {
            for (Race race : races) {
                List<Alignment> alignments = race.getCrewAlignment();
                for (Alignment alignment : alignments) {
                    List<CrewMember> crewMembers = alignment.getCrew().getCrewMembers();
                    if ( crewMembers != null ) {
                        for (CrewMember crewMember : crewMembers) {
                            deletePerson(crewMember.getPerson().getId());
                        }
                    }
                    deleteCrewMember(alignment.getCrew().getId());
                    deleteCrew(alignment.getCrew().getId());
                }
            }
        }
        deleteAlignment(rowingEvent.getId());
        deleteRace(rowingEvent.getId());
        deleteRowingEvent(rowingEvent.getId());
    }

    /**
     * Create race tree information
     * @param race
     */
    private void createRaceTree(Race race) {
        List<Alignment> alignments = race.getCrewAlignment();
        for (Alignment alignment : alignments) {
            createAlignmentTree(alignment);
        }
        createRace(race);
        if ( race.getBoatType() != null && !existBoatType(race.getBoatType().getBoatId()) ) {
            createBoatType(race.getBoatType());
        }
        if ( race.getCategory()!= null && !existCategory(race.getCategory().getId()) ) {
            createCategory(race.getCategory());
        }
        // Create rest of the tree (starting by alignment
    }

    private void createAlignmentTree(Alignment alignment) {
        createCrewTree(alignment.getCrew());
        createAlignment(alignment);
    }

    private void createCrewTree(Crew crew) {
        if (!existCompetitor(crew.getCompetitor().getId())) {
            createCompetitor(crew.getCompetitor());
        }
        List<CrewMember> crewMembers = crew.getCrewMembers();
        for (CrewMember crewMember: crewMembers) {
            if (!existPerson(crewMember.getPerson().getId())) {
                createPerson(crewMember.getPerson());
            }
            createCrewMember(crewMember);
        }
        if ( !existCrew(crew.getId()) ) {
            createCrew(crew);
        }
    }


    // CRUD - Create Read Update Delete

    /**
     * Create a new Rowing event with all info associated
     *
     * @param rowingEvent
     * @return
     */
    public RowingEvent createRowingEvent(RowingEvent rowingEvent) {
        Log.d(TAG,"createRowingEvent()");
        ContentValues values = new ContentValues();

        values.put("id", rowingEvent.getId());
        values.put("name", rowingEvent.getName());
        if (rowingEvent.getEventDate() != null) {
            values.put("event_date", rowingEvent.getEventDate().toString());
        }
        if (rowingEvent.getChangeMoment() != null) {
            values.put("change_moment", rowingEvent.getChangeMoment().toString());
        }
        values.put("location", rowingEvent.getLocation());

        long lastId = db.insert("rowing_event",null, values);

        return rowingEvent;
    }

    public void deleteRowingEvent(String eventId) {
        int result = db.delete("rowing_event", "id=?", new String[] { eventId });
    }



    /**
     * Read the rowing event information
     *
     * @param eventId
     * @return
     */
    public RowingEvent readRowingEvent(String eventId) {
        Log.d(TAG,"readRowingEvent()");


        Cursor c = db.rawQuery("SELECT * FROM rowing_event WHERE id = '" + eventId + "'", null);

        if(c.getCount() == 0)
            return null;

        c.moveToFirst();
        RowingEvent rowingEvent = new RowingEvent();

        rowingEvent.setId(c.getString(c.getColumnIndex("id")));
        rowingEvent.setName(c.getString(c.getColumnIndex("name")));
        // TODO : Fix the dates
        // rowingEvent.setEventDate(c.getString(c.getColumnIndex("event_date")));
        // rowingEvent.setChangeMoment(c.getString(c.getColumnIndex("hange_moment")));
        rowingEvent.setLocation(c.getString(c.getColumnIndex("location")));
        rowingEvent.setEventRaces(readRaces(eventId));
        c.close();
        return rowingEvent;
    }

    public List<RowingEvent> readRowingEventList() {
        Log.d(TAG,"readRowingEventList()");


        ArrayList<RowingEvent> rowingEvents = new ArrayList<RowingEvent>();
        Cursor c = db.rawQuery(
                "SELECT * FROM rowingEvent order by event_date ",
                null);

        if(c.getCount() == 0)
            return null;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            RowingEvent rowingEvent = new RowingEvent();

            rowingEvent.setId(c.getString(c.getColumnIndex("crew_id")));
            rowingEvent.setName(c.getString(c.getColumnIndex("name")));
            /// TODO : Handle dates
            //rowingEvent.setEventDate(c.getString(c.getColumnIndex("event_date")));
            rowingEvent.setLocation(c.getString(c.getColumnIndex("location")));
            rowingEvents.add(rowingEvent);
        }
        c.close();
        return rowingEvents;
    }

    /**
     * TODO : Fix events that have been deleted on server
     *
     * @param rowingEvents
     */
    public void insertRowingEventList(List<RowingEvent> rowingEvents) {
        for (RowingEvent rowingEvent : rowingEvents) {
            if ( !existRowingEvent(rowingEvent.getId()))  {
                createRowingEvent(rowingEvent);
            }
        }
    }

    public Boolean existRowingEvent(String eventId) {
        Log.d(TAG,"existRowingEvent()");

        Boolean retval = false;
        Cursor c = db.rawQuery("SELECT * FROM rowing_event WHERE boat_id = '" + eventId + "'", null);

        if(c.getCount() > 0)
            retval = true;
        c.close();
        return retval;
    }

    public void update(RowingEvent rowingEvent) {

    }


    // Race

    /**
     * Create a new Rowing event with all info associated
     *
     * @param race
     * @return
     */
    public Race createRace(Race race) {
        Log.d(TAG,"createRace()");
        ContentValues values = new ContentValues();

        values.put("event_id", race.getEventId());
        values.put("seqno", race.getSeqno());
        if ( race.getHour() != null )
            values.put("hour", race.getHour().toString());
        if ( race.getBoatType() != null )
            values.put("boat_type", race.getBoatType().getBoatId());
        if ( race.getCategory() != null )
            values.put("category", race.getCategory().getId());

        long lastId = db.insert("race",null, values);

        return race;
    }

    /**
     * Read the races of a rowing event
     *
     * @param eventId
     * @return
     */
    public List<Race> readRaces(String eventId) {
        Log.d(TAG,"readRaces()");

        ArrayList<Race> races = new ArrayList<Race>();
        Cursor c = db.rawQuery("SELECT * FROM race WHERE event_id = '" + eventId + "'", null);

        if(c.getCount() == 0)
            return null;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Race race = new Race();

            race.setEventId(c.getString(c.getColumnIndex("event_id")));
            race.setSeqno(c.getInt(c.getColumnIndex("seqno")));
            // TODO : Fix the date
            // race.setEventDate(c.getString(c.getColumnIndex("hour")));
            race.setBoatType(readBoatType(c.getString(c.getColumnIndex("boat_type"))));
            race.setCategory(readCategory(c.getString(c.getColumnIndex("category"))));
            race.setCrewAlignment(readCrewAlignment(c.getString(c.getColumnIndex("event_id")),race.getSeqno()));
            races.add(race);
        }
        c.close();
        return races;
    }

    public void deleteRace(String eventId) {
        int result = db.delete("race", "event_id=?", new String[] { eventId });
    }


    // Alignment

    /**
     * Create a new Rowing event with all info associated
     *
     * @param alignment
     * @return
     */
    public Alignment createAlignment(Alignment alignment) {
        Log.d(TAG,"createAlignment()");
        ContentValues values = new ContentValues();

        values.put("event_id", alignment.getEventId());
        values.put("race_no", alignment.getRaceNo());
        values.put("lane", alignment.getLane());
        values.put("crew", alignment.getCrew().getId());

        long lastId = db.insert("alignment",null, values);

        return alignment;
    }

    /**
     * Read the races of a rowing event
     *
     * @param eventId
     * @return
     */
    public List<Alignment> readCrewAlignment(String eventId, Integer raceno) {
        Log.d(TAG,"readCrewAlignment()");


        ArrayList<Alignment> alignments = new ArrayList<Alignment>();
        Cursor c = db.rawQuery(
                "SELECT * FROM alignment WHERE event_id = '" + eventId + "' and race_no=" + raceno +
                " order by lane ",
                null);

        if(c.getCount() == 0)
            return null;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Alignment alignment = new Alignment();

            alignment.setEventId(c.getString(c.getColumnIndex("event_id")));
            alignment.setRaceNo(c.getInt(c.getColumnIndex("race_no")));
            alignment.setLane(c.getInt(c.getColumnIndex("lane")));
            alignment.setCrew(readCrew(c.getString(c.getColumnIndex("crew"))));
            alignments.add(alignment);
        }
        c.close();
        return alignments;
    }

    public void deleteAlignment(String eventId) {
        int result = db.delete("alignment", "event_id=?", new String[] { eventId });
    }


    // Boat_Type
    /**
     * Create a new Rowing event with all info associated
     *
     * @param boatType
     * @return
     */
    public BoatType createBoatType(BoatType boatType) {
        Log.d(TAG,"createBoatType()");
        ContentValues values = new ContentValues();

        values.put("boat_id", boatType.getBoatId());
        values.put("name", boatType.getName());

        long lastId = db.insert("boat_type",null, values);

        return boatType;
    }

    /**
     * Read the Boat Type information
     *
     * @param boatId
     * @return
     */
    public BoatType readBoatType(String boatId) {
        Log.d(TAG,"readBoatType()");

        Cursor c = db.rawQuery("SELECT * FROM boat_type WHERE boat_id = '" + boatId + "'", null);

        if(c.getCount() == 0)
            return null;

        c.moveToFirst();
        BoatType boatType = new BoatType();

        boatType.setBoatId(c.getString(c.getColumnIndex("boat_id")));
        boatType.setName(c.getString(c.getColumnIndex("name")));
        c.close();
        return boatType;
    }

    /**
     * Check if a boat type exist
     *
     * @param boatId
     * @return
     */
    public Boolean existBoatType(String boatId) {
        Log.d(TAG,"readBoatType()");

        Boolean retval = false;
        Cursor c = db.rawQuery("SELECT * FROM boat_type WHERE boat_id = '" + boatId + "'", null);

        if(c.getCount() > 0)
            retval = true;
        c.close();
        return retval;
    }


    // Category
    /**
     * Create a new Rowing event with all info associated
     *
     * @param category
     * @return
     */
    public Category createCategory(Category category) {
        Log.d(TAG,"createCategory()");
        ContentValues values = new ContentValues();

        values.put("id", category.getId());
        values.put("name", category.getName());
        values.put("gender", category.getGender());

        long lastId = db.insert("category",null, values);

        return category;
    }

    /**
     * Read the Category information
     *
     * @param categoryId
     * @return
     */
    public Category readCategory(String categoryId) {
        Log.d(TAG,"readCategory()");

        Cursor c = db.rawQuery("SELECT * FROM category WHERE id = '" + categoryId + "'", null);

        if(c.getCount() == 0)
            return null;

        c.moveToFirst();
        Category category = new Category();

        category.setId(c.getString(c.getColumnIndex("id")));
        category.setName(c.getString(c.getColumnIndex("name")));
        category.setGender(c.getString(c.getColumnIndex("gender")));
        c.close();
        return category;
    }

    /**
     * Check if a category exist
     *
     * @param categoryId
     * @return
     */
    public Boolean existCategory(String categoryId) {
        Log.d(TAG,"existCategory()");

        Boolean retval = false;
        Cursor c = db.rawQuery("SELECT * FROM category WHERE category_id = '" + categoryId + "'", null);

        if(c.getCount() > 0)
            retval = true;
        c.close();
        return retval;
    }



    // Competitor
    /**
     * Create a new Rowing event with all info associated
     *
     * @param competitor
     * @return
     */
    public Competitor createCompetitor(Competitor competitor) {
        Log.d(TAG,"createCompetitor()");
        ContentValues values = new ContentValues();

        values.put("id", competitor.getId());
        values.put("name", competitor.getName());
        values.put("acronym", competitor.getAcronym());

        long lastId = db.insert("competitor",null, values);

        return competitor;
    }


    /**
     * Read the Category information
     *
     * @param competitorId
     * @return
     */
    public Competitor readCompetitor(String competitorId) {
        Log.d(TAG,"readCompetitor()");

        Cursor c = db.rawQuery("SELECT * FROM competitor WHERE id = '" + competitorId + "'", null);

        if(c.getCount() == 0)
            return null;

        c.moveToFirst();
        Competitor  competitor  = new Competitor ();

        competitor.setId(c.getString(c.getColumnIndex("id")));
        competitor.setName(c.getString(c.getColumnIndex("name")));
        competitor.setAcronym(c.getString(c.getColumnIndex("acronym")));
        c.close();
        return competitor;
    }

    /**
     * Check if a boat type exist
     *
     * @param competitorId
     * @return
     */
    public Boolean existCompetitor(String competitorId) {
        Log.d(TAG,"existCompetitor()");

        Boolean retval = false;
        Cursor c = db.rawQuery("SELECT * FROM competitor WHERE id = '" + competitorId + "'", null);

        if(c.getCount() > 0)
            retval = true;
        c.close();
        return retval;
    }


    // Crew
    /**
     * Create a new Rowing event with all info associated
     *
     * @param crew
     * @return
     */
    public Crew createCrew(Crew crew) {
        Log.d(TAG,"createCompetitor()");
        ContentValues values = new ContentValues();

        values.put("id", crew.getId());
        values.put("competitor_id", crew.getCompetitor().getId());
        values.put("description", crew.getDescription());

        long lastId = db.insert("crew",null, values);

        return crew;
    }

    /**
     * Read the Category information
     *
     * @param crewId
     * @return
     */
    public Crew readCrew(String crewId) {
        Log.d(TAG,"readCrew()");

        Cursor c = db.rawQuery("SELECT * FROM crew WHERE id = '" + crewId + "'", null);

        if(c.getCount() == 0)
            return null;

        c.moveToFirst();
        Crew crew = new Crew();

        crew.setId(c.getString(c.getColumnIndex("id")));
        crew.setCompetitor(readCompetitor(c.getString(c.getColumnIndex("competitor_id"))));
        crew.setDescription(c.getString(c.getColumnIndex("description")));
        crew.setCrewMembers(readCrewMembers(c.getString(c.getColumnIndex("id"))));
        c.close();
        return crew;
    }

    public void deleteCrew(String crewId) {
        int result = db.delete("crew", "id=?", new String[] { crewId });
    }

    public Boolean existCrew(String crewId) {
        Log.d(TAG,"existCrew()");

        Boolean retval = false;
        Cursor c = db.rawQuery("SELECT * FROM crew WHERE id = '" + crewId + "'", null);

        if(c.getCount() > 0)
            retval = true;
        c.close();
        return retval;
    }


    // Crew_member
    /**
     * Create a new Rowing event with all info associated
     *
     * @param crewMember
     * @return
     */
    public CrewMember createCrewMember(CrewMember crewMember) {
        Log.d(TAG,"createCompetitor()");
        ContentValues values = new ContentValues();

        values.put("id", crewMember.getCrewId());
        values.put("position", crewMember.getPosition());
        values.put("person_id", crewMember.getPerson().getId());

        long lastId = db.insert("crew",null, values);

        return crewMember;
    }

    /**
     * Read the races of a rowing event
     *
     * @param crewId
     * @return
     */
    public List<CrewMember> readCrewMembers(String crewId) {
        Log.d(TAG,"readCrewMembers()");


        ArrayList<CrewMember> crewMembers = new ArrayList<CrewMember>();
        Cursor c = db.rawQuery(
                "SELECT * FROM crew_member WHERE crew_id = '" + crewId + "'" + " order by position ",
                null);

        if(c.getCount() == 0)
            return null;

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            CrewMember crewMember = new CrewMember();

            crewMember.setCrewId(c.getString(c.getColumnIndex("crew_id")));
            crewMember.setPosition(c.getInt(c.getColumnIndex("position")));
            crewMember.setPerson(readPerson(c.getString(c.getColumnIndex("person_id"))));
            crewMembers.add(crewMember);
        }
        c.close();
        return crewMembers;
    }

    public void deleteCrewMember(String crewId) {
        int result = db.delete("crew_member", "crew_id=?", new String[] { crewId });
    }

    // Person
    /**
     * Create a new Rowing event with all info associated
     *
     * @param person
     * @return
     */
    public Person createPerson(Person person) {
        Log.d(TAG,"createCompetitor()");
        ContentValues values = new ContentValues();

        values.put("id", person.getId());
        values.put("name", person.getName());

        long lastId = db.insert("crew",null, values);

        return person;
    }

    /**
     * Read the Category information
     *
     * @param personId
     * @return
     */
    public Person readPerson(String personId) {
        Log.d(TAG,"readPerson()");

        Cursor c = db.rawQuery("SELECT * FROM person WHERE id = '" + personId + "'", null);

        if(c.getCount() == 0)
            return null;

        c.moveToFirst();
        Person person = new Person ();

        person.setId(c.getString(c.getColumnIndex("id")));
        person.setName(c.getString(c.getColumnIndex("name")));
        c.close();
        return person;
    }

    public void deletePerson(String personId) {
        int result = db.delete("person", "id=?", new String[] { personId });
    }

    /**
     * Check if a person exist
     *
     * @param personId
     * @return
     */
    public Boolean existPerson(String personId) {
        Log.d(TAG,"existPerson()");

        Boolean retval = false;
        Cursor c = db.rawQuery("SELECT * FROM competitor WHERE id = '" + personId + "'", null);

        if(c.getCount() > 0)
            retval = true;
        c.close();
        return retval;
    }

}
