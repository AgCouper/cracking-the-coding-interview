-- Implement an algorithm to determine if a string has all unique characters.

module Alg (isUniqueChars) where
import Data.List
import Data.Maybe
import Data.Array

stringToIntList :: String -> [Int]
stringToIntList = map fromEnum

stringToFrequencyArray s = accumArray (+) 0 (0, 255) (zip (stringToIntList s) [1, 1..])

isUniqueChars s = isNothing $ find (>1) (stringToFrequencyArray s)

