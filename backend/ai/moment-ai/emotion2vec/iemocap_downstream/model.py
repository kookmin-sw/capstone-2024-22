import torch
from torch import nn

class BaseModel(nn.Module):
    def __init__(self, input_dim=768, output_dim=5):
        super().__init__()
        # for 2 linear
        self.pre_net = nn.Linear(input_dim, 256)

        self.post_net = nn.Linear(256, output_dim)
        
        self.activate = nn.ReLU()
        
        # for 1 linear
        self.net = nn.Linear(input_dim, output_dim)

    def forward(self, x, padding_mask=None):
        # for 2 linear
        x = self.post_net(self.activate(self.pre_net(x)))
        
        ## for 1 linear
        # x = self.net(x)

        # x = x * (1 - padding_mask.unsqueeze(-1).float())
        # x = x.sum(dim=1) / (1 - padding_mask.float()
        #                     ).sum(dim=1, keepdim=True)  # Compute average
        
        # x = self.post_net(x)
        return x
