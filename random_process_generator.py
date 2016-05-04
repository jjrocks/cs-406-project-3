import random as rn

num_lines = 5
num_process = 3
mem_length = 30000
read_write = ["R", "W"]
file_name = "test.txt"

f = open(file_name, 'w')
file_text = ""
for x in range(num_lines):
	file_text += str(rn.randrange(num_process)+1)+", " + str(rn.randrange(mem_length+1)) + ", " + rn.choice(read_write) + "\n"

f.write(file_text)
