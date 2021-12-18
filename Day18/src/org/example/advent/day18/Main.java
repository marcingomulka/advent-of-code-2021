package org.example.advent.day18;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();
        List<Node> trees = lines.stream()
                .map(Main::parseTree)
                .collect(Collectors.toList());

        Node addition = trees.stream()
                .map(Node::copy)
                .reduce(Main::addTrees)
                .orElseThrow(IllegalArgumentException::new);
        System.out.println("Part1: " + magnitude(addition));

        long maxSum = 0L;
        for (int i = 0; i < trees.size(); i++) {
            for (int j = 0; j < trees.size(); j++) {
                if (i != j) {
                    long sum = magnitude(addTrees(trees.get(i).copy(), trees.get(j).copy()));
                    if (sum > maxSum) {
                        maxSum = sum;
                    }
                }
            }
        }
        System.out.println("Part2: " + maxSum);
    }

    private static long magnitude(Node master) {
        if (master.isLeaf()) {
            return master.number;
        } else {
            return 3L * magnitude(master.left) + 2L * magnitude(master.right);
        }
    }

    private static Node addTrees(Node tree1, Node tree2) {
        Node master = Node.node(tree1, tree2);
        return reduce(master);
    }

    private static Node reduce(Node master) {
        boolean actionDone = true;
        while (actionDone) {
            if (!explode(master)) {
                actionDone = split(master);
            }
        }
        return master;
    }

    private static boolean explode(Node master) {
        Optional<Node> foundExplosion = searchForExplosion(master, 0);
        if (foundExplosion.isPresent()) {
            Node toExplode = foundExplosion.get();
            int leftInt = toExplode.left.number;
            int rightInt = toExplode.right.number;

            searchPrecedingNode(toExplode.left, master)
                    .ifPresent(before -> before.number += leftInt);
            searchFollowingNode(toExplode.right, master)
                    .ifPresent(after -> after.number += rightInt);

            toExplode.number = 0;
            toExplode.left = null;
            toExplode.right = null;
            return true;
        }
        return false;
    }

    private static Optional<Node> searchForExplosion(Node master, int level) {
        Node left = master.left;
        Node right = master.right;
        if (master.isLeaf()) {
            return Optional.empty();
        }
        if (left.isLeaf() && right.isLeaf() && level >= 4) {
            return Optional.of(master);
        } else {
            return searchForExplosion(left, level + 1)
                    .or(() -> searchForExplosion(right, level + 1));
        }
    }

    private static boolean split(Node master) {
        List<Node> leaves = leaves(master);
        for (Node n : leaves) {
            if (n.number > 9) {
                Node splitLeft = Node.leaf((int) Math.floor((float) n.number / 2.0));
                Node splitRight = Node.leaf((int) Math.ceil((float) n.number / 2.0));
                splitLeft.parent = n;
                splitRight.parent = n;
                n.number = null;
                n.left = splitLeft;
                n.right = splitRight;
                return true;
            }
        }
        return false;
    }


    private static Optional<Node> searchPrecedingNode(Node node, Node root) {
        List<Node> leafs = leaves(root);
        int i = leafs.indexOf(node) - 1;
        return i >= 0 ? Optional.of(leafs.get(i)) : Optional.empty();
    }

    private static Optional<Node> searchFollowingNode(Node node, Node root) {
        List<Node> leafs = leaves(root);
        int i = leafs.indexOf(node) + 1;
        return i < leafs.size() ? Optional.of(leafs.get(i)) : Optional.empty();
    }

    private static List<Node> leaves(Node root) {
        List<Node> leaves = new ArrayList<>();
        if (root.isLeaf()) {
            leaves.add(root);
        } else {
            leaves.addAll(leaves(root.left));
            leaves.addAll(leaves(root.right));
        }
        return leaves;
    }

    public static Node parseTree(String line) {
        line = line.substring(1, line.length() - 1);

        int division = findDivisionPoint(line);
        String leftPart = line.substring(0, division).trim();
        String rightPart = line.substring(division + 1).trim();
        Node left;
        if (!leftPart.contains(",")) {
            left = Node.leaf(Integer.valueOf(leftPart));
        } else {
            left = parseTree(leftPart);
        }
        Node right;
        if (!rightPart.contains(",")) {
            right = Node.leaf(Integer.valueOf(rightPart));
        } else {
            right = parseTree(rightPart);
        }
        return Node.node(left, right);
    }

    private static int findDivisionPoint(String line) {
        List<String> chars = line.chars().mapToObj(Character::toString).collect(Collectors.toList());
        Deque<String> stack = new LinkedList<>();
        for (int i = 0; i < chars.size(); i++) {
            String s = chars.get(i);
            if (stack.isEmpty() && s.equals(",")) {
                return i;
            }
            if (s.equals("[")) {
                stack.push(s);
            } else if (s.equals("]")) {
                stack.pop();
            }
        }
        return -1;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}

class Node {
    Integer number;
    Node parent;
    Node left;
    Node right;

    public Node(Integer number, Node left, Node right) {
        this.number = number;
        this.left = left;
        this.right = right;
    }

    public static Node leaf(int value) {
        return new Node(value, null, null);
    }

    public static Node node(Node left, Node right) {
        var node = new Node(null, left, right);
        left.parent = node;
        right.parent = node;
        return node;
    }

    public boolean isLeaf() {
        return number != null;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node copy() {
        Node left = null;
        Node right = null;
        if (this.left != null) {
            left = this.left.copy();
        }
        if (this.right != null) {
            right = this.right.copy();
        }
        Node n = new Node(number, left, right);
        if (right != null) {
            right.parent = n;
        }
        if (left != null) {
            left.parent = n;
        }
        return n;
    }

    @Override
    public String toString() {
        if (number != null) {
            return number.toString();
        } else {
            return "[" + left.toString() + "," + right.toString() + "]";
        }
    }
}