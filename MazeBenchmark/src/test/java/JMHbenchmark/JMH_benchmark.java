package JMHbenchmark;

import GUI.MazeGenerator;
import MazeEngine.Maze;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;



@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)










public class JMH_benchmark {

    @Param({"10","25","50"})
    public int size;


    int[][] grid;
    Maze maze1;
    Maze maze2;

    Maze.Node start1;
    Maze.Node end1;
    Maze.Node start2;
    Maze.Node end2;


    @Setup(Level.Trial)
    public void generateMaze(){
        grid = new MazeGenerator(size, size).getGrid();
        maze1 = new Maze(grid);
        maze2 = new Maze(grid);
        start1 = maze1.getStartNode();
        end1 = maze1.getEndNode();
        start2 = maze2.getStartNode();
        end2 = maze2.getEndNode();

    }


    @Benchmark
    public void AstarPriorityQueue(){

        maze1.traverseA_star(start1,end1);


    }

    @Benchmark
    public void AstarLinkedList(){
        maze2.traverseA_starList(start2,end2);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_benchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }




}
