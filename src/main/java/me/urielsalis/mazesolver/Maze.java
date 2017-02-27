package me.urielsalis.mazesolver;

import java.util.ArrayList;

/**
 * Sapienchat 2017(c)
 *
 * @author Uriel Salischiker
 * @package me.urielsalis.mazesolver
 */
public class Maze {
    public Node start;
    public Node end;
    private ArrayList<Node> tempNodes = new ArrayList();
    private boolean[][] image;
    private int width;
    private int height;

    public Maze(boolean[][] image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;
        boolean foundStart = false;
        boolean foundEnd = false;
        for(int i = 0; i < width; i++) {
            if(image[0][i] && !foundStart) {
                start = new Node(0, i);
                foundStart = true;
            }
            if(image[height][i] && !foundEnd) {
                end = new Node(height, i);
                foundEnd = true;
            }
            if(foundStart && foundEnd) break;
        }
        if(!foundStart || !foundEnd) {
            System.err.println("Start or End not found");
            System.exit(1);
        }
        tempNodes.add(start);
        tempNodes.add(end);
        for (int y = 1; y < height-1; y++) {
            for(int x=0; x < width; y++) {
                if(image[y][x]) {
                    boolean up = image[y-1][x];
                    boolean down = image[y-1][x];
                    boolean left = image[y][x-1];
                    boolean right = image[y][x+1];
                    int count = 0;
                    if(up) count++;
                    if(down) count++;
                    if(left) count++;
                    if(right) count++;
                    if(count==1 || count==3 || count==4 || (count==2 && !(up&&down)) || (count==2 && !(left&&right))) {
                        Node node = new Node(y, x);
                        tempNodes.add(findNeighboars(node));
                    }
                }
            }
        }
        findLimits();
    }

    private void findLimits() {
        for(Node node: tempNodes) {
            if(node.x==start.x && node.y==start.y) {
                start = node;
            }
            if(node.x==end.x && node.y==end.y) {
                end = node;
            }
        }
    }

    private Node findNeighboars(Node node) {
        for(Node node1: tempNodes) {
            if(node1.x==node.x) node = checkY(node1, node);
            if(node1.y==node.y) node = checkX(node1, node);
        }
        return node;
    }

    private Node checkX(Node node1, Node node) {
        if(node.x > node1.x) {
            for (int x = node1.x; x < node.x; x++) {
                if(image[node.y][x]) {
                    return node;
                }
            }
            if(node.leftNode == null || (node.x - node1.x < node.distanceLeft)) {
                node.leftNode = node1;
                node.distanceLeft = node.x - node1.x;
                node1.rightNode = node;
                node1.distanceRight = node.distanceLeft;
            }
            return node;
        } else {
            for (int x = node.x; x < node1.x; x++) {
                if(image[node.y][x]) {
                    return node;
                }
            }
            if(node.rightNode == null || (node1.x - node.x < node.distanceRight)) {
                node.rightNode = node1;
                node.distanceRight = node1.x - node.x;
                node1.leftNode = node;
                node1.distanceLeft = node.distanceRight;
            }
            return node;
        }
    }

    private Node checkY(Node node1, Node node) {
        if(node.y > node1.y) {
            for (int y = node1.y; y < node.y; y++) {
                if(image[y][node.x]) {
                    return node;
                }
            }
            if(node.downNode == null || (node.y - node1.y < node.distanceDown)) {
                node.downNode = node1;
                node.distanceDown = node.y - node1.y;
                node1.upNode = node;
                node1.distanceUp = node.distanceDown;
            }
            return node;
        } else {
            for (int y = node.y; y < node1.y; y++) {
                if(image[y][node.x]) {
                    return node;
                }
            }
            if(node.upNode == null || (node1.y - node.y < node.distanceUp)) {
                node.upNode = node1;
                node.distanceUp = node1.y - node.y;
                node1.downNode = node;
                node1.distanceDown = node.distanceUp;
            }
            return node;
        }
    }

    private class Node {
        public Node upNode;
        int distanceUp = 0;
        public Node downNode;
        int distanceDown = 0;
        public Node leftNode;
        int distanceLeft = 0;
        public Node rightNode;
        int distanceRight = 0;
        public int x;
        public int y;

        public Node(int y, int x) {
            this.x = x;
            this.y = y;
        }
    }
}
