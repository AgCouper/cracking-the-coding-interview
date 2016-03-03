-- Given two strings, write a method to decide
-- if one is a permutation of the other.

module Alg (isStringPerm, randomTest) where
import Data.List
import Data.Maybe
import Data.Array
import Test.QuickCheck

stringToIntList :: String -> [Int]
stringToIntList = map fromEnum

stringToFrequencyArray s = accumArray (+) 0 (0, 255) (zip (stringToIntList s) [1, 1..])

isStringPerm s1 s2
    | length s1 /= length s2 = False
    | otherwise = isNothing $ find (uncurry (/=)) (zip (elems $ stringToFrequencyArray s1) (elems $ stringToFrequencyArray s2))

randomTest = quickCheck(\s1 s2 -> if sort s1 == sort s2 then isStringPerm s1 s2 else not (isStringPerm s1 s2))

