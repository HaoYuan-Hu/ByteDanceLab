package com.byted.camp.todolist;


import android.view.View;

import com.byted.camp.todolist.beans.Note;

public interface NoteOperator {

    void deleteNote(Note note);

    void updateNote(Note note);

    void changeContentNote(Note note,View v);
}
