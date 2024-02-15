CPATH='.:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar'

rm -rf student-submission
rm -rf grading-area

mkdir grading-area

git clone $1 student-submission 2> "grading-area/clone-output.txt"

if [[ $? -ne 0 ]]
then
    echo "============================================================"
    cat clone-output.txt
    echo "============================================================"
    echo "Failed to clone repository"
    echo "Score: 0%"
    exit 1
fi

echo "Successfully cloned repository"

if ! [[ -f student-submission/ListExamples.java ]]
then
    echo "ListExamples.java not found"
    echo "Score: 0%"
    exit 1
fi

cp student-submission/ListExamples.java grading-area/
cp TestListExamples.java grading-area/

cd grading-area

CLASSPATH=".:../lib/hamcrest-core-1.3.jar:../lib/junit-4.13.2.jar"

javac -cp $CLASSPATH *.java > "compile-output.txt" 2>&1

if [[ $? -ne 0 ]]
then
    echo "============================================================"
    cat compile-output.txt
    echo "============================================================"
    echo "Failed to compile, see error above"
    echo "Score: 0%"
    exit 1
fi

echo "Compiled successfully"
echo "Running tests"

java -cp $CLASSPATH org.junit.runner.JUnitCore TestListExamples > test-output.txt

LAST_LINE=$(tail -n2 test-output.txt | head -n1)

echo $LAST_LINE | grep "OK" -q

if [[ $? -eq 0 ]]
then
    TESTS=$(echo $LAST_LINE | cut -d' ' -f 2 | cut -d'(' -f 2)
    echo "All $TESTS test(s) passed"
    echo "Score: 100%"
    exit 0
fi

TESTS=$(echo $LAST_LINE | cut -d' ' -f 3 | cut -d',' -f 1)
FAILED=$(echo $LAST_LINE | cut -d' ' -f 5)
PASSED=$((TESTS - FAILED))
SCORE=$(echo "scale=1 ; $((PASSED * 100)) / $TESTS" | bc)

# print out failed tests
echo "Failed test(s) output:"
echo "============================================================"
cat test-output.txt | grep -A1 "[0-9]) test"
echo "============================================================"

echo "Passed $PASSED/$TESTS tests"
echo "Score: $SCORE%"

