package com.moictab.valenciacultural.controller;

import com.moictab.valenciacultural.model.Block;
import com.moictab.valenciacultural.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moict on 11/12/2017.
 */

public class BlocksController {

    public List<Block> getBlocks(List<Event> events) {

        List<Block> blocks = new ArrayList<>();

        for (Event event : events) {
            if (!checkIfExists(event, blocks)) {
                Block block = new Block(event.category, event);
                blocks.add(block);
            } else {
                insertIntoBlock(event, blocks);
            }
        }

        return blocks;
    }

    private boolean checkIfExists(Event event, List<Block> blocks) {
        for (Block block : blocks) {
            if (event.category.equals(block.title)) {
                return true;
            }
        }

        return false;
    }

    private void insertIntoBlock(Event event, List<Block> blocks) {
        for (Block block : blocks) {
            if (block.title.equals(event.category)) {
                block.events.add(event);
            }
        }
    }

}
